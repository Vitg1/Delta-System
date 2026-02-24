/* @Author Sr Mil Games */
public class DeltaApplyMenu extends FilesPanelFileMenu{
    public DeltaApplyMenu(){
        super("Apply delta rules");
    }

    public void onClick(File file){
        if (file == null || !file.getName().endsWith(".delta")){
            Terminal.log("Selecione um arquivo");
            return;
        }

        DeltaConfig config = DeltaApply.parseDelta(file);
        if (config.targets == null){
            Terminal.log("Delta sem target");
            return;
        }

        if (file == null || !file.getName().endsWith(".delta")) return;
        DeltaRunner.run(config);
    }

    public boolean filterFormat(String format){
        return format.endsWith(".delta");
    }}