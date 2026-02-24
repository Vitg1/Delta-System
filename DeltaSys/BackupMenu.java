/* @Author Unknown user */
public class BackupMenu extends FilesPanelFileMenu {  

    public BackupMenu() {  
        super("Do Backup");  
    }  

    @Override
    public void onClick(File file){  
        if (file == null || !file.exists()) return;

        // Checa se é realmente um .bak
        if (!file.getName().endsWith(".bak")) return;

        try {
            // Determina o arquivo original
            String originalPath = file.getAbsolutePath().substring(0, file.getAbsolutePath().length() - 4);
            File original = new File(originalPath);

            // Confirma sobrescrita (pode substituir com diálogo na UI)
            if (original.exists()) {
                // Aqui você pode colocar um diálogo de confirmação, exemplo:
                // boolean ok = showConfirm("Deseja restaurar o backup e sobrescrever " + original.getName() + "?");
                // if (!ok) return;

                Toast.showText("Restaurando backup: \n" + file.getName() + " → " + original.getName(),2);
            }

            // Copia o .bak para o arquivo original, sobrescrevendo
            java.nio.file.Files.copy(file.toPath(), original.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);

            Toast.showText("Backup restaurado com sucesso!",2);

        } catch (Exception e) {
            Terminal.log(e);
        }
    }  

    @Override
    public boolean filterFormat(String format){  
        return format.endsWith(".bak");  
    }  
}