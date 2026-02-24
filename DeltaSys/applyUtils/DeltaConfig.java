package JAVARuntime;
import java.util.*;
import java.text.*;
import java.net.*;
import java.math.*;
import java.io.*;
import java.nio.*;
import java.nio.*;
import java.util.HashMap;
import java.util.Map;
/* @Author Sr Mil Games */
public class DeltaConfig{
    List<String> targets = new ArrayList<String>();
    List<String> excludes = new ArrayList<String>();
    List<String> commentLinesWith = new ArrayList<String>();
    List<String> uncommentLinesWith = new ArrayList<String>();
    String onlyIfContains;
    boolean dryRun = false;
    boolean format= false;
    boolean auto = false;
    public String logs = "normal";
    public String logsLang = "ptbr";
    public String logPrefix = "[DELTA]";
    public boolean caseSensitive = false;
    boolean createBackup = false;
    
    boolean previewLines=false;
    List<DeltaReplaceRule> rules = new ArrayList<DeltaReplaceRule>();
    public DeltaConfig() {
       excludes.add("DeltaSys/*");
    }
}