/* @Author Sr Mil Games */
public class DeltaCreateMenu extends FilesPanelDirectoryMenu{
    public DeltaCreateMenu(){
        super("Create .delta");
    }

    public void onClick(File directory){
        if (directory == null || !directory.isDirectory()) return;
        final File dir = directory;
        new InputDialog(
        "Criar arquivo",
        "nome_do_arquivo",
        "Cancelar",
        "Criar",
        new InputDialogListener(){
            public void onFinish(String valor){
                if (valor == null) return;
                String name = valor.trim();
                if (name.isEmpty()) return;
                if (!name.endsWith(".delta")){
                    name += ".delta";
                }

                File deltaFile = new File(dir, name);
                try{
                    if (deltaFile.exists()) return;
                    deltaFile.createNewFile();
                    FileWriter writer = new FileWriter(deltaFile);
                    writer.write("// Delta File\n\n");
                    writer.write("logsLang:ptbr //eng\n\n");
                    writer.write("logs:debug //normal,none\n\n");
                    writer.write("target:myScript.java //pasta/*.java\n\n");
                    writer.write("dryRun:true\n\n");
                    writer.write("format:true");
                    writer.close();
                } catch (Exception e){
                    e.printStackTrace();
                }

            }
            public void onCancel(){
            }

        }
        );
    }

}