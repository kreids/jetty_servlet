package test.edu.upenn.cis455;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class RunAllTests extends TestCase 
{
  public static Test suite() 
  {
    try {
      Class[]  testClasses = {
    	Class.forName("edu.upenn.cis455.servlet.HttpClientTest"),
    	Class.forName("edu.upenn.cis455.servlet.XPathServletTest"),
    	Class.forName("edu.upenn.cis455.storage.DBWrapperTest"),
    	Class.forName("edu.upenn.cis455.storage.DBWrapperTest2"),
    	Class.forName("edu.upenn.cis455.xpathengine.AttXPathEngineImplTest"),
    	Class.forName("edu.upenn.cis455.xpathengine.BasicXPathEngineImplTest"),
    	Class.forName("edu.upenn.cis455.xpathengine.ContainsXPathEngineImplTest"),
    	Class.forName("edu.upenn.cis455.xpathengine.EqualsXPathEngineImplTest")
    	
        /* TODO: Add the names of your unit test classes here */
        // Class.forName("your.class.name.here") 
      };   
      
      return new TestSuite(testClasses);
    } catch(Exception e){
      e.printStackTrace();
    } 
    
    return null;
  }
}
