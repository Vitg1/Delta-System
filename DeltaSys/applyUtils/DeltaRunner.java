/* @Author Sr Mil Games */
public class DeltaRunner{
    public static void run(DeltaConfig config){
        File root = new File(Directories.getProjectFolder() + "/Files/");
        applyRecursive(root, config);
    }

    private static void applyRecursive(File dir, DeltaConfig config){
        File[] files = dir.listFiles();
        if (files == null) return;
        for (int i = 0; i < files.length; i++){
            File f = files[i];
            if (f.isDirectory()){
                applyRecursive(f, config);
                continue;
            }

            if (DeltaApply.matchExclude(f, config.excludes)) continue;
            for (int t = 0; t < config.targets.size(); t++){
                if (DeltaApply.matchTarget(f, config.targets.get(t))){
                    DeltaApply.applyDeltaToFile(f, config);
                    break;
                }

            }
        }

    }
}

