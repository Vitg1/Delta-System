/* @Author Unknown user */
public class DeltaBakSetIcon extends FilesPanelCustomIcon {
@Override
    public boolean supportFile(File file){
        return file != null && file.getName().endsWith(".bak");
    }

    @Override
    public File getIconForFile(File file){
        File root = new File(Directories.getProjectFolder()+"/DeltaSys/"); // ponto inicial da busca
        File icon = findIcon(root);
        if (icon != null && icon.exists()){
            return icon;
        }

        // fallback caso não encontre
        return new File(Directories.getProjectFolder()+"/icons/DeltaBakLogo.png");
    }

    private File findIcon(File dir){
        if (dir == null || !dir.exists()) return null;
        File[] files = dir.listFiles();
        if (files == null) return null;
        for (File f : files){
            if (f.isDirectory()){
                // achou a pasta ImageIcon
                if (f.getName().equalsIgnoreCase("DeltaImageIcons")){
                    File icon = new File(f, "DeltaBakLogo.png");
                    if (icon.exists()) return icon;
                }

                // continua procurando dentro
                File result = findIcon(f);
                if (result != null) return result;
            }

        }
        return null;
    }
}