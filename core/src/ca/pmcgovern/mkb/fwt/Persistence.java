package ca.pmcgovern.mkb.fwt;

import ca.pmcgovern.mkb.util.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Json;

public class Persistence {

    
    private Persistence() {
        
        if( Gdx.files == null || !Gdx.files.isLocalStorageAvailable()) {
            // TODO: log this case
            this.dataDir = System.getProperty( "java.io.tmpdir" );
            
        } else {
            
            this.dataDir = Gdx.files.getLocalStoragePath();
        }
        
        // TODO: log case
    }
    
    
    private static Persistence singleton;
    
    private String dataDir;
    
    public static Persistence getInstance() {
        
        if( singleton == null ) {
            init();
        }
        
        return singleton;
    }
    
    
    private synchronized static void init() {
        singleton = new Persistence();
    }
    
    public String getPath() {
        return this.dataDir;
    }
    
    public boolean dataFileExists( String filePrefix ) {

        if( filePrefix == null || filePrefix.isEmpty() ) {
            throw new IllegalArgumentException( "File prefix is null or empty." );
        }
        
        File localDir = new File( this.dataDir );
        
        if( !localDir.exists() ) {
            return false;
        }
               
        File dataFile = new File( localDir, filePrefix + ".json" );
        
        return dataFile.exists();
                        
    }
    
    public List readList( String filePrefix) /* , Class<List<?>> clazz ) */
            throws IOException {
     
        if( filePrefix == null || filePrefix.isEmpty() ) {
            throw new IllegalArgumentException( "File prefix is null or empty." );
        }
        
        File localDir = new File( this.dataDir );
        
        if( !localDir.exists() ) {
            throw new RuntimeException( "Data dir " + this.dataDir + " Does not exist." );
        }
        
        if( !localDir.isDirectory() ) {
            throw new RuntimeException( "Data dir not a directory: " + localDir );
        }  
        
        File dataFile = new File( localDir, filePrefix + ".json" );
        
        if( !dataFile.exists() || dataFile.length() == 0 ) {
            return null;
        }
        
            
        Json json = new Json();
        List ret = json.fromJson( ArrayList.class,  new FileInputStream( dataFile ));
        
        return ret;
    }
    
    
    public boolean deleteFile( String filePrefix ) throws IOException {
        if( filePrefix == null || filePrefix.isEmpty() ) {
            throw new IllegalArgumentException( "File prefix is null or empty." );
        }
        
        File localDir = new File( this.dataDir );
        
        if( !localDir.exists() ) {
            throw new RuntimeException( "Data dir " + this.dataDir + " Does not exist." );
        }
        
        if( !localDir.isDirectory() ) {
            throw new RuntimeException( "Data dir not a directory: " + localDir );
        }   
        
        File target = new File( localDir, filePrefix + ".json" );
        
        boolean goodDeletion = false;
        
        if( target.exists() && target.isFile() ) {
            goodDeletion = target.delete();
        }
        
        return goodDeletion;
    }
    
    
    
    public long writeList( String filePrefix, List<?> items ) throws IOException {
    
        long fileSize = 0;
        
        if( filePrefix == null || filePrefix.isEmpty() ) {
            throw new IllegalArgumentException( "File prefix is null or empty." );
        }
        
        if( items == null || items.isEmpty() ) {
            return 0;
        }
                
        File localDir = new File( this.dataDir );
        
        if( !localDir.exists() ) {
            throw new RuntimeException( "Data dir " + this.dataDir + " Does not exist." );
        }
        
        if( !localDir.isDirectory() ) {
            throw new RuntimeException( "Data dir not a directory: " + localDir );
        }  
        
        
        File tmp = null;
        PrintWriter tmpWriter = null;
        
        try {
            
            tmp = File.createTempFile( "tasks-01", "tmp", localDir );            
            tmpWriter = new PrintWriter( tmp );
        
            Json json = new Json();            
            json.toJson( items, tmpWriter );
            
            tmpWriter.flush();
                       
            File dest = new File( localDir, filePrefix + ".json" );
            
            // Thanks, Bill.
            if( System.getProperty( "os.name" ).contains( "Windows" )) {
                dest.delete();
            }
                        
            if( !tmp.renameTo( dest )) {
                throw new RuntimeException( "Failed to copy tmp file to " + dest ); 
            }
            
            fileSize = dest.length();
            
        } finally {
            
            if( tmpWriter != null ) {
                
                try {
                    tmpWriter.close();
                } catch( Exception e ) {}
            }
            
            if( tmp != null && tmp.exists() ) {
                tmp.delete();
            }
        }        
        
        return fileSize;
    }
    
}
