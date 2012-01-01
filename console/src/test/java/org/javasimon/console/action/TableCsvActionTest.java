package org.javasimon.console.action;

//import org.javasimon.console.TestActionContext;
import org.javasimon.console.SimonData;
import org.javasimon.console.TestActionContext;
import org.testng.annotations.*;
import static org.testng.Assert.*;

/**
 * Unit test for {@link TableCsvAction}
 * @author gquintana
 */
public class TableCsvActionTest {
    @BeforeClass
    public static void setUpClass() {
        SimonData.initialize();
    }
    @Test
    public void testExecute() throws Exception {
        TestActionContext context=new TestActionContext("/data/table.csv");
        TableCsvAction action=new TableCsvAction(context);
        action.execute();
        assertEquals(context.getContentType(),"text/csv");
        String csv=context.toString();
        assertTrue(csv.contains("\"A\",\"STOPWATCH\",3,600,100,200,300,300,81"));
        assertTrue(csv.contains("\"B\",\"STOPWATCH\",2,300,100,150,100,200,50"));
        assertTrue(csv.contains("\"C\",\"STOPWATCH\",1,300,300,300,300,300,0"));
    }
    @Test
    public void testExecutePattern() throws Exception {
        TestActionContext context=new TestActionContext("/data/table.csv");
        context.setParameter("pattern", "A*");
        TableCsvAction action=new TableCsvAction(context);
        action.readParameters();
        action.execute();
        assertEquals(context.getContentType(),"text/csv");
        String csv=context.toString();
        assertTrue(csv.contains("\"A\",\"STOPWATCH\",3,600,100,200,300,300,81"));
        assertFalse(csv.contains("\"B\",\"STOPWATCH\",2,300,100,150,100,200,50"));
        assertFalse(csv.contains("\"C\",\"STOPWATCH\",1,300,300,300,300,300,0"));
    }
    @Test
    public void testExecuteType() throws Exception {
        TestActionContext context=new TestActionContext("/data/table.csv");
        context.setParameter("type", "COUNTER");
        TableCsvAction action=new TableCsvAction(context);
        action.readParameters();
        action.execute();
        assertEquals(context.getContentType(),"text/csv");
        String csv=context.toString();
        assertFalse(csv.contains("\"A\",\"STOPWATCH\",3,600,100,200,300,300,81"));
        assertFalse(csv.contains("\"B\",\"STOPWATCH\",2,300,100,150,100,200,50"));
        assertFalse(csv.contains("\"C\",\"STOPWATCH\",1,300,300,300,300,300,0"));
    }
}
