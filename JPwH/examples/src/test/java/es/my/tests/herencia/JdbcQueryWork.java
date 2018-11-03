/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.my.tests.herencia;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import org.testng.Assert;

/**
 *
 * @author fran
 */
public class JdbcQueryWork implements org.hibernate.jdbc.Work {

    private final boolean    matchOrder;
    private final String[][] expectedRows;
    private final String     sql;

    /**************************************************************************/
    /*                       Metodos Privados                                 */
    /**************************************************************************/
    private boolean _assertMatch(String[] actualRow, String[] expectedRow) {
        for (int i = 0; i < expectedRow.length; i++)
        {
            String regex = expectedRow[i];
            String actual = actualRow[i];

            if (regex == null)
            {
                if (actual == null) continue;
                return false;
            }
            if (actual == null || !actual.matches(regex)) return false;
        }

        return true;
    }

    private String[] _getNextRow(ResultSet result) throws SQLException {
        int colCount = result.getMetaData().getColumnCount();
        String[] row = new String[colCount];

        for (int colNo = 0; colNo < colCount; colNo++)
        {
            // JDBC index is 1-based, not 0-based.
            row[colNo] = result.getString(colNo + 1);
        }

        return row;
    }

    private int _assertOrderedResults(ResultSet result) throws SQLException {
        int rowIdx = 0;

        while (result.next())
        {
            String[] expectedRow = this.expectedRows[rowIdx];

            for (int colNo = 0; colNo < expectedRow.length; colNo++)
            {
                // JDBC index is 1-based, not 0-based.
                String actual = result.getString(colNo + 1);
                String expected = expectedRow[colNo];
                Assert.assertTrue(actual.matches(expected));
            }
            rowIdx++;
        }
        return rowIdx;
    }

    private int _assertUnorderedResults(ResultSet result) throws SQLException {
        final List<String[]> expectedRowList = new LinkedList<String[]>(Arrays.asList(this.expectedRows));
        int rowCount = 0;

        while (result.next())
        {
            String[] actualRow = this._getNextRow(result);
            int i = 0;
            boolean matchRow = false;

            for (String[] expectedRow : expectedRowList)
            {
                if (matchRow = this._assertMatch(actualRow, expectedRow))
                {
                    expectedRowList.remove(i);
                    break;
                }
                i++;
            }
            if (!matchRow) Assert.fail(String.format("Unexpected row: %s", Arrays.toString(actualRow)));

            rowCount++;
        }

        if (!expectedRowList.isEmpty()) Assert.fail("Expected rows to match: " + expectedRowList);

        return rowCount;
    }

    /**************************************************************************/
    /*                       Metodos Protegidos                               */
    /**************************************************************************/
    protected long _copy(Reader input, Writer output) throws IOException {
        char[] buffer = new char[4096];
        long   count  = 0;

        int n;

        while (-1 != (n = input.read(buffer)))
        {
            output.write(buffer, 0, n);
            count += n;
        }

        return count;
    }

    protected String _getTextResourceAsString(String resource) throws IOException {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(resource);

        if (is == null) throw new IllegalArgumentException("Resource not found: " + resource);

        StringWriter sw = new StringWriter();
        _copy(new InputStreamReader(is), sw);

        return sw.toString();
    }

    /**************************************************************************/
    /*                          Constructores                                 */
    /**************************************************************************/
    public JdbcQueryWork(final String sqlResource, final boolean matchOrder, final String[]... expectedRows) throws IOException {
        this.sql          = _getTextResourceAsString(sqlResource);
        this.expectedRows = expectedRows;
        this.matchOrder   = matchOrder;
    }

    /**************************************************************************/
    /*                       Metodos Publicos                                 */
    /**************************************************************************/
    @Override
    public void execute(Connection connection) throws SQLException {
        final PreparedStatement st = connection.prepareStatement(this.sql);

        try
        {
            final ResultSet rs = st.executeQuery();

            Assert.assertEquals(rs.getMetaData().getColumnCount(), this.expectedRows[0].length, "Unexpected column count!!");

            int rowCount = this.matchOrder ? _assertOrderedResults(rs) : _assertUnorderedResults(rs);

            Assert.assertEquals(rowCount, this.expectedRows.length);

            rs.close();
        }
        finally {st.close();}
    }
}
