/* @Author Sr Mil Games */
public class DeltaAutoRunner{
    static boolean ranThisCompile = false;
    public static void onAfterCompile(){
        if (ranThisCompile) return;
        ranThisCompile = true;
        runAllAutoDeltas();
    }

    public static void onBeforeCompile(){
        ranThisCompile = false;
    }

    static void runAllAutoDeltas(){
        File base = new File(Directories.getProjectFolder() + "/Files/");
        findDeltasRecursive(base);
    }

    static void findDeltasRecursive(File dir){
        File[] files = dir.listFiles();
        if (files == null) return;
        for (int i = 0; i < files.length; i++){
            File f = files[i];
            if (f.isDirectory()){
                findDeltasRecursive(f);
                continue;
            }

            if (f.getName().endsWith(".delta")){
                processDelta(f);
            }

        }
    }

    static void processDelta(File deltaFile){
        DeltaConfig config = DeltaApply.parseDelta(deltaFile);
        if (!config.auto) return;
        Terminal.log("[AUTO DELTA]: " + deltaFile.getName());
        DeltaRunner.run(config);
    }

}
