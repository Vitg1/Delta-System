/* @Author Sr Mil Games */
public class DeltaEditor extends TextScriptingExtension{
    private File loadedFile;
    TextScriptingTheme theme = new TextScriptingTheme();
    int autosave;
    @Override
    public void replaceScript(File newScript){
        loadScript(newScript);
    }

    @Override
    public void openScript(File script){
        loadScript(script);
    }

    @Override
    public void afterTextChanged(android.text.Editable a){
        autosave++;
        if (autosave > 5){
            autosave = 0;
            saveScript();
        }

    }
    public void loadScript(File f){
        try{
            createTheme();
            formatTextManual();
            loadedFile = f;
            String text = FileLoader.loadTextFromFile(f);
            super.setText(text);
        } catch (Exception e){
            Terminal.log(e);
        }

    }
    @Override
    public boolean hasScript(){
        return loadedFile != null;
    }

    @Override
    public boolean saveScript(){
        try{
            String text = super.getText();
            FileLoader.exportTextToFile(text, loadedFile);
            return true;
        } catch (Exception e){
            Terminal.log(e);
            return false;
        }

    }
    String getLine(int line){
        String txt = getText();
        if (txt == null) return null;
        String[] lines = txt.split("\n", -1);
        if (line < 0 || line >= lines.length)
        return null;
        return lines[line];
    }

    static String detectLangFromText(String fullText){
        if (fullText == null) return "ptbr";
        String[] lines = fullText.split("\n");
        for (String l : lines){
            if (l == null) continue;
            String s = stripInlineComment(l);
            if (s == null) continue;
            s = s.trim().toLowerCase();
            if (s.startsWith("logslang:")){
                String lang = s.substring("logslang:".length()).trim();
                if (LanguageManager.translations.containsKey(lang))
                return lang;
            }

        }
        return "ptbr"; // fallback
    }

    // Coloque essas funções na sua classe de editor (DeltaEditor ou similar).
    @Override
    public TextScriptingExtension.LineTip getTipForLine(int line){
        String full = getText();
        LanguageManager.currentLang = detectLangFromText(full);
        String l=getLine(line-1);
        if (isComment(l))
        return TextScriptingExtension.LineTip.None;
        int q=analyzeQuotes(l);
        if (q!=0)
        return TextScriptingExtension.LineTip.Alert;
        if (isUnknownCommand(l))
        return TextScriptingExtension.LineTip.Alert;
        if (isCommandContentEmpty(l))
        return TextScriptingExtension.LineTip.Alert;
        return TextScriptingExtension.LineTip.None;
    }

    static final Set<String> VALID_COMMANDS = new HashSet<String>(Arrays.asList(
    "replace",
    "remove",
    "target",
    "exclude",
    "onlyifcontains",
    "commentlineswith",
    "uncommentlineswith",
    "dryrun",
    "format",
    "auto",
    "previewlines",
    "logs",
    "logslang",
    "logprefix",
    "casesensitive",
    "backup"
    ));
    static String extractCommand(String line){
        if (line == null) return null;
        int idx = line.indexOf(':');
        if (idx == -1) return null;
        String cmd = line.substring(0, idx).trim().toLowerCase();
        if (cmd.length()==0) return null;
        return cmd;
    }

    static boolean isComment(String line){
        if (line == null) return false;
        int i = 0;
        while (i < line.length() && Character.isWhitespace(line.charAt(i)))
        i++;
        if (i >= line.length()) return false;
        if (line.startsWith("//", i)) return true;
        if (line.startsWith("#", i)) return true;
        return false;
    }

    static String stripInlineComment(String line){
        if (line == null) return null;
        boolean inQuote = false;
        for (int i=0;i<line.length();i++){
            char c = line.charAt(i);
            if (c == '"'){
                inQuote = !inQuote;
                continue;
            }

            if (!inQuote){
                // detecta //
                if (c == '/' && i+1 < line.length() && line.charAt(i+1)=='/'){
                    return line.substring(0,i).trim();
                }

                // detecta #
                if (c == '#'){
                    return line.substring(0,i).trim();
                }

            }
        }

        return line.trim();
    }

    static boolean isUnknownCommand(String line){
        if (line == null) return false;
        String s = stripInlineComment(line);
        if (s == null) return false;
        s = s.trim();
        if (s.length()==0) return false;
        // linha só texto sem :
        if (s.indexOf(":")==-1)
        return true;
        String lower = s.toLowerCase();
        for (String cmd : VALID_COMMANDS){
            if (lower.startsWith(cmd))
            return false;
        }

        return true;
    }

    static int analyzeQuotes(String body){
        if (body == null || body.trim().isEmpty())
        return 0;
        if (hasOddQuotes(body))
        return 2; // erro real
        if (hasTooManyQuotes(body))
        return 1; // estranho
        return 0; // ok
    }

    static boolean hasTooManyQuotes(String s){
        int q = countQuotes(s);
        return q > 4; // ajusta se quiser mais rígido
    }

    static boolean isCommandContentEmpty(String line){
        if (line==null) return false;
        int idx = line.indexOf(':');
        if (idx == -1) return false; // nao é comando com :
        String after = line.substring(idx + 1).trim();
        return after.isEmpty();
    }

    @Override
    public String getTipTextForLine(int line){
        String l=getLine(line);
        int q=analyzeQuotes(l);
        if (q==1)
        return LanguageManager.tr("TIP_TOO_MANY_QUOTES");
        if (q==2)
        return LanguageManager.tr("TIP_ODD_QUOTES");
        if (isUnknownCommand(l))
        return LanguageManager.tr("TIP_UNKNOWN_COMMAND");
        if (isCommandContentEmpty(l))
        return LanguageManager.tr("TIP_EMPTY_COMMAND");
        
        return LanguageManager.tr("TIP_ALL_GOOD");
    }

    static int countQuotes(String s){
        if (s == null || s.length()==0) return 0;
        int count = 0;
        boolean escape = false;
        for (int i=0;i<s.length();i++){
            char c = s.charAt(i);
            if (escape){
                escape = false;
                continue;
            }

            if (c == '\\'){
                escape = true;
                continue;
            }

            if (c == '"')
            count++;
        }

        return count;
    }

    static boolean hasOddQuotes(String s){
        return (countQuotes(s) % 2) != 0;
    }

    @Override
    public boolean supportFile(File file){
        return file.getName().endsWith(".delta");
    }

    private void formatTextManual(){
        String text = getText();
        if (text == null) return;
        String[] lines = text.split("\n", -1);
        StringBuilder out = new StringBuilder();
        String last = null;
        for (int i = 0; i < lines.length; i++){
            String line = rtrim(lines[i]);
            if (last != null && last.isEmpty() && line.isEmpty()){
                continue; // remove vazias duplicadas
            }

            out.append(line);
            if (i < lines.length - 1) out.append("\n");
            last = line;
        }

        setText(out.toString());
    }

    private String rtrim(String s){
        int end = s.length();
        while (end > 0 && Character.isWhitespace(s.charAt(end - 1))) end--;
        return s.substring(0, end);
    }

    private void createTheme(){
        // backgroundColor (12,12,12)
        theme.backgroundColor.setFloats(12f / 255f, 12f / 255f, 12f / 255f);
        // textColor (220,220,220)
        theme.textColor.setFloats(220f / 255f, 220f / 255f, 220f / 255f);
        // selectedLineColor (28,28,28)
        theme.selectedLineColor.setFloats(28f / 255f, 28f / 255f, 28f / 255f);
        // selectionColor (50,90,140)
        theme.selectionColor.setFloats(50f / 255f, 90f / 255f, 140f / 255f);
        // gutterColor (18,18,18)
        theme.gutterColor.setFloats(18f / 255f, 18f / 255f, 18f / 255f);
        // gutterTextColor (140,140,140)
        theme.gutterTextColor.setFloats(140f / 255f, 140f / 255f, 140f / 255f);
        // gutterDividerColor (40,40,40)
        theme.gutterDividerColor.setFloats(40f / 255f, 40f / 255f, 40f / 255f);
        // gutterCurrentLineNumberColor (220,180,120)
        theme.gutterCurrentLineNumberColor.setFloats(220f / 255f, 180f / 255f, 120f / 255f);
        // findResultBackgroundColor (160,140,60)
        theme.findResultBackgroundColor.setFloats(160f / 255f, 140f / 255f, 60f / 255f);
        setTheme(theme);
    }

}