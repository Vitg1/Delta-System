/* @Author Unknown user */
import java.util.*;

public class LanguageManager{
    // traduções centralizadas
    static Map<String, Map<String, String>> translations = new HashMap<String, Map<String, String>>();
    static String currentLang="ptbr";
    
    public static String tr(String key, Object... args){
        String lang = currentLang;
        if (lang == null || !translations.containsKey(lang))
            lang = "ptbr";
        Map<String,String> dict = translations.get(lang);
        String template = dict.getOrDefault(key, key);
        try{
            return String.format(template, args);
        }catch (Exception e){
            return template + " " + Arrays.toString(args);
        }
    }

    static{
        Map<String, String> ptbr = new HashMap<String, String>();
        ptbr.put("APPLY_DELTA", "Aplicando delta em: %s");
        ptbr.put("SKIP_ONLYIF", "Pulando onlyIfContains: %s");
        ptbr.put("WILDCARD_RULE", "Wildcard regra: %s");
        ptbr.put("REPLACE_HIT", "Regra aplicada: %s");
        ptbr.put("REPLACE_LOOP_ABORT", "Loop de replace abortado (segurança)");
        ptbr.put("DRY_RUN", "DRY RUN: alteraria %s (%d mudanças)");
        ptbr.put("DELTA_APPLIED", "Delta aplicado em %s (%d mudanças)");
        ptbr.put("NO_CHANGES", "Nenhuma alteração em %s");
        ptbr.put("FORMAT", "Formatado: %s");
        ptbr.put("TARGET_ADDED", "Target: %s");
        ptbr.put("EXCLUDE_ADDED", "Exclude: %s");
        ptbr.put("ONLYIF_SET", "onlyIfContains: %s");
        ptbr.put("COMMENT_ADDED", "Comment: %s");
        ptbr.put("UNCOMMENT_ADDED", "Uncomment: %s");
        ptbr.put("REMOVE_RULE", "Remove: %s");
        ptbr.put("REPLACE_ADDED", "Replace: %s >> %s");
        ptbr.put("REPLACE_INVALID", "Replace inválido linha %d");
        ptbr.put("WARN_REPLACE_INVALID", "Replace inválido linha %d");
        ptbr.put("LINE_IGNORED", "Linha ignorada: %s");
        ptbr.put("PARSE_COMPLETE", "Delta parseado. Regras: %d");
        ptbr.put("PREVIEW_FILE", "Preview do arquivo: %s");
        ptbr.put("PREVIEW_NO_CHANGES", "Nenhuma mudança detectada em %s");
        ptbr.put("PREVIEW_CHANGES_TOTAL", "Total de mudanças: %s");
        ptbr.put("PREVIEW_RULE_HEADER", "Regra: %s >> %s");
        ptbr.put("PREVIEW_APPLYING_RULE", "Aplicando regra: %s");
        ptbr.put("PREVIEW_STATS", "Arquivos: %d | Mudanças: %d | Tempo: %dms");
        ptbr.put("ERROR_READING_FILE", "Erro lendo arquivo: %s");
        ptbr.put("ERROR_APPLYING", "Erro aplicando delta: %s");
        ptbr.put("TIP_UNKNOWN_COMMAND","Comando desconhecido, esta linha será ignorada");
        ptbr.put("TIP_EMPTY_COMMAND","Comando sem conteúdo, será ignorado");
        ptbr.put("TIP_ODD_QUOTES","Aspas ímpares — tem certeza? Isso pode causar comportamento indesejado");
        ptbr.put("TIP_TOO_MANY_QUOTES","Muitas aspas emboladas — use separadores se precisar de aspas internas");
        ptbr.put("TIP_ALL_GOOD","Nada a constar, tenha um bom dia :)");

        // Novas chaves de log / warnings
        ptbr.put("WILDCARD_FULL_FILE_IGNORED", "Regra wildcard ignorada (apagar arquivo inteiro é perigoso): %s");
        ptbr.put("SKIP_LARGE_FILE", "Pulando arquivo grande: %s (%d bytes)");
        ptbr.put("BACKUP_CREATED", "Backup criado: %s");
        ptbr.put("BACKUP_FAILED", "Falha ao criar backup para: %s");
        ptbr.put("MULTILINE_WILDCARD_HIT", "Wildcard multilinha aplicado: %s");
        ptbr.put("SINGLELINE_WILDCARD_HIT", "Wildcard linha-única aplicado: %s");
        ptbr.put("SKIP_INTERNAL", "Pulando arquivo interno do sistema: %s");

        translations.put("ptbr", ptbr);
        
        Map<String, String> eng = new HashMap<String, String>();
        eng.put("APPLY_DELTA", "Applying delta on: %s");
        eng.put("SKIP_ONLYIF", "Skipping onlyIfContains: %s");
        eng.put("WILDCARD_RULE", "Wildcard rule: %s");
        eng.put("REPLACE_HIT", "Rule matched: %s");
        eng.put("REPLACE_LOOP_ABORT", "Replace loop aborted (safety)");
        eng.put("DRY_RUN", "DRY RUN: would change %s (%d changes)");
        eng.put("DELTA_APPLIED", "Delta applied on %s (%d changes)");
        eng.put("NO_CHANGES", "No changes in %s");
        eng.put("FORMAT", "Formatted: %s");
        eng.put("TARGET_ADDED", "Target: %s");
        eng.put("EXCLUDE_ADDED", "Exclude: %s");
        eng.put("ONLYIF_SET", "onlyIfContains: %s");
        eng.put("COMMENT_ADDED", "Comment: %s");
        eng.put("UNCOMMENT_ADDED", "Uncomment: %s");
        eng.put("REMOVE_RULE", "Remove: %s");
        eng.put("REPLACE_ADDED", "Replace: %s >> %s");
        eng.put("REPLACE_INVALID", "Invalid replace at line %d");
        eng.put("WARN_REPLACE_INVALID", "Invalid replace at line %d");
        eng.put("LINE_IGNORED", "Line ignored: %s");
        eng.put("PARSE_COMPLETE", "Delta parsed. Rules: %d");
        eng.put("PREVIEW_FILE", "Previewing file: %s");
        eng.put("PREVIEW_NO_CHANGES", "No changes detected in %s");
        eng.put("PREVIEW_CHANGES_TOTAL", "Total changes: %s");
        eng.put("PREVIEW_RULE_HEADER", "Rule: %s >> %s");
        eng.put("PREVIEW_APPLYING_RULE", "Applying rule: %s");
        eng.put("PREVIEW_STATS", "Files: %d | Changes: %d | Time: %dms");
        eng.put("ERROR_READING_FILE", "Error reading file: %s");
        eng.put("ERROR_APPLYING", "Error applying delta: %s");
        eng.put("TIP_UNKNOWN_COMMAND","Unknown command, this line will be ignored");
        eng.put("TIP_EMPTY_COMMAND","Command has no content, it will be ignored");
        eng.put("TIP_ODD_QUOTES","Unmatched quotes — are you sure? This may cause unexpected behavior");
        eng.put("TIP_TOO_MANY_QUOTES","Too many tangled quotes — use separators if you need inner quotes");
        eng.put("TIP_ALL_GOOD","Nothing to report, have a nice day :)");

        // New keys
        eng.put("WILDCARD_FULL_FILE_IGNORED", "Wildcard rule ignored (would delete entire file): %s");
        eng.put("SKIP_LARGE_FILE", "Skipping large file: %s (%d bytes)");
        eng.put("BACKUP_CREATED", "Backup created: %s");
        eng.put("BACKUP_FAILED", "Backup failed for: %s");
        eng.put("MULTILINE_WILDCARD_HIT", "Multi-line wildcard applied: %s");
        eng.put("SINGLELINE_WILDCARD_HIT", "Single-line wildcard applied: %s");
        eng.put("SKIP_INTERNAL", "Skipping internal system file: %s");

        translations.put("eng", eng);
    }
}