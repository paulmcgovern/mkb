package ca.pmcgovern.mkb.util;


import java.util.List;
import java.util.ArrayList;
import java.util.Date;

import org.testng.annotations.BeforeClass;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;

import ca.pmcgovern.mkb.ui.Task;
import ca.pmcgovern.mkb.fwt.Task.IconColor;
import ca.pmcgovern.mkb.ui.Task.TaskState;
import ca.pmcgovern.mkb.ui.Task.Type;
import ca.pmcgovern.mkb.util.Persistence;

@Test
public class PersistanceTest {

    private List<Task> allTasks;
    private String dataFilePrefix;
    
    @BeforeClass    
    public void buildTasks() {
    
        this.allTasks = new ArrayList<Task>();
        
        Type[] allTypes = Type.values();
        IconColor[] allIconColors = IconColor.values();
       
        
        for( int i = 1; i <= allTypes.length; i++ ) {
            
            for( int j = 0; j < allIconColors.length; j++ ) {
                
                Task t = new Task();
            
                t.setDescription( "Task " + i );
                t.setId( i  );
                t.setPosX( i );
                t.setPosY( i );
                t.setState( TaskState.NEW );
                t.setType( allTypes[ i - 1 ] );
                
              //  t.setColour( allIconColors[ j ] );
                
               this.allTasks.add( t ); 
            }
        }
        
        Reporter.log( "Created " + this.allTasks.size() + " tasks for tests: " + allTasks, true );
            
        this.dataFilePrefix = Long.toString( System.currentTimeMillis());
        
        Reporter.log( "Data file prefix: " + this.dataFilePrefix );    
    }
    
    
    @Test
    public void testWrite() throws IOException {
                
      
        Persistence p = Persistence.getInstance();
  
        Assert.assertNotNull( p.getPath(), "Local storage path is null" );
        
        Reporter.log( "Local storage dir: " + p.getPath(), true );
       
        long bytesWritten = p.writeList( this.dataFilePrefix,  this.allTasks );
       
       Assert.assertTrue( bytesWritten > 0, "Failed to write anything." );
      
       
     
       File dataFile = new File( p.getPath() + File.separator +  this.dataFilePrefix + ".json");
       
       Assert.assertTrue( dataFile.exists(), "Data file does not exist " + dataFile );
       
       Reporter.log( "Wrote " + bytesWritten + " bytes.", true );
   
       Assert.assertEquals( dataFile.length(), bytesWritten, "Data file length mismatch." );
       
       
       
    }
    
    @Test( dependsOnMethods={ "testWrite" } )
    public void testRead() throws IOException {
        
        Persistence p = Persistence.getInstance();
        
        Assert.assertNotNull( p.getPath(), "Local storage path is null" );
        
       
        List<? extends Object> readTasks = p.readList( this.dataFilePrefix );//, ArrayList<Task>.class );
        
        Reporter.log( "Local storage dir: " + p.getPath(), true );              
       
        Assert.assertNotNull(  readTasks, "Failed to read anything." );
     
        int readCount = readTasks.size();
        
        Assert.assertEquals( readCount, this.allTasks.size(), "Faild to read all records." );
        
        Reporter.log( "Read " + readTasks.size() + " records." );
        
       
        for( int i = 0; i < readCount; i++ ) {            
            Assert.assertEquals( readTasks.get( i ), this.allTasks.get( i ), "Tasks not equal " + readTasks.get( i ) + " vs. " + this.allTasks.get( i ) );
        }
        
    }
    
  
}
