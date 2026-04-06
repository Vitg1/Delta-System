package JAVARuntime;
import java.util.*;
import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/* @Author Sr Mil Games */
public class DeltaApply {
  public static boolean matchTarget(File file, String target) {
    if (file == null || target == null) return false;
    String name = file.getName();
    target = target.trim();
    if (target.startsWith("[") && target.endsWith("]")) {
      target = target.substring(1, target.length() - 1).trim();
    }

    if (target.startsWith("\"") && target.endsWith("\"")) {
      target = target.substring(1, target.length() - 1);
    }
    String base;
    
    base = new File(Directories.getProjectFolder() + "/Files/").getAbsolutePath().replace("\\", "/");

   String path = file.getAbsolutePath().replace("\\", "/");
    String relative = path.replace(base, "");
    if (relative.startsWith("/")) {
      relative = relative.substring(1);
    }

    if (target.startsWith("*.")) {
      return file.getName().endsWith(target.substring(1));
    }

    if (target.contains("/*.")) {
      String folder = target.substring(0, target.indexOf("/*."));
      String ext = target.substring(target.indexOf("*.") + 1);
      return relative.startsWith(folder + "/") && file.getName().endsWith(ext);
    }

    if (target.endsWith("/*")) {
      String folder = target.substring(0, target.length() - 2);
      return relative.startsWith(folder + "/");
    }

    return file.getName().equals(target);
  }

  public static boolean matchExclude(File file, List<String> excludes) {
    for (int i = 0; i < excludes.size(); i++) {
      if (matchTarget(file, excludes.get(i))) {
        return true;
      }
    }
    return false;
  }

  static String supremeFormat(String text) {
    if (text == null) return null;
    String[] lines = text.split("\n", -1);
    StringBuilder out = new StringBuilder();
    int indentLevel = 0;
    String indentStr = "    ";
    boolean lastLineEmpty = false;
    for (String rawLine : lines) {
      String line = rawLine.trim();
      if (line.isEmpty()) {
        lastLineEmpty = true;
        continue;
      }

      if (line.startsWith("}")) {
        indentLevel = Math.max(0, indentLevel - 1);
      }

      for (int j = 0; j < indentLevel; j++) {
        out.append(indentStr);
      }

      line = line.replaceAll("\\b(if|for|while|switch|catch)\\(", "$1 (");
      line = line.replaceAll("\\s+([;\\{])", "$1");
      out.append(line).append("\n");
      if (line.endsWith("{")) indentLevel++;
      if (line.startsWith("}") && !lastLineEmpty) {
        out.append("\n");
        lastLineEmpty = true;
      } else lastLineEmpty = false;
    }
    return out.toString();
  }


// ----------------- APLICAR DELTA (versão melhorada) -----------------
public static void applyDeltaToFile(File file, DeltaConfig config) {
    if (file == null || config == null) return;
    log(config, "debug", "APPLY_DELTA", file.getName());

    try {
       // Proteção interna: nunca processar arquivos do sistema
        if (matchTarget(file, "DeltaSys/*")) {
            log(config, "debug", "SKIP_INTERNAL", file.getName());
            return;
        }

        // Proteção por tamanho (evita processar arquivos gigantes por engano)
        long maxBytes = 10 * 1024 * 1024; // 10 MB, ajuste se necessário
        if (file.length() > maxBytes) {
            log(config, "warn", "SKIP_LARGE_FILE", file.getName(), file.length());
            return;
        }

        String original = FileLoader.loadTextFromFile(file);
        if (original == null) return;

        // SELEÇÃO DE ARQUIVO (onlyIfContains) - respeita Case Sensitivity
        if (config.onlyIfContains != null && !config.onlyIfContains.isEmpty()) {
            boolean found;
            if (config.caseSensitive) {
                found = original.contains(config.onlyIfContains);
            } else {
                found = original.toLowerCase().contains(config.onlyIfContains.toLowerCase());
            }

            if (!found) {
                log(config, "debug", "SKIP_ONLYIF", file.getName());
                return;
            }
        }

        String modified = original;
        int changes = 0;

        // ITERAR REGRAS
        for (int i = 0; i < config.rules.size(); i++) {
            DeltaReplaceRule rule = config.rules.get(i);
            if (rule == null || rule.from == null) continue;

            String from = rule.from;
            String to = rule.to == null ? "" : rule.to;
            String before = modified;

            // --- LÓGICA WILDCARD MULTI-LINE (....) ---
            if (from.contains("....")) {
                String[] parts = from.split("\\.\\.\\.\\.", -1);
                String startToken = parts.length > 0 ? parts[0] : "";
                String endToken = parts.length > 1 ? parts[1] : "";

                // Proteção: evitar '....' vazio que apaga o arquivo inteiro
                if (startToken.trim().isEmpty() && endToken.trim().isEmpty()) {
                    log(config, "warn", "WILDCARD_FULL_FILE_IGNORED", from);
                    continue;
                }

                String searchText = config.caseSensitive ? modified : modified.toLowerCase();
                String sTokenSearch = config.caseSensitive ? startToken : startToken.toLowerCase();
                String eTokenSearch = config.caseSensitive ? endToken : endToken.toLowerCase();

                int searchFrom = 0;
                boolean anyReplaced = false;

                // Loop para substituir múltiplas ocorrências de forma segura
                while (true) {
                    int s = sTokenSearch.isEmpty() ? 0 : searchText.indexOf(sTokenSearch, searchFrom);
                    if (s == -1) break;

                    int e;
                    if (!eTokenSearch.isEmpty()) {
                        int offset = s + sTokenSearch.length();
                        int relativeE = searchText.indexOf(eTokenSearch, offset);
                        if (relativeE == -1) {
                            e = modified.length();
                        } else {
                            e = relativeE + eTokenSearch.length();
                        }
                    } else {
                        e = modified.length();
                    }

                    if (s < e) {
                        modified = modified.substring(0, s) + to + modified.substring(e);
                        changes++;
                        anyReplaced = true;
                        // recomputa searchText e continua a busca logo após a substituição para evitar loops
                        searchText = config.caseSensitive ? modified : modified.toLowerCase();
                        searchFrom = s + to.length();
                    } else {
                        break;
                    }
                }

                if (anyReplaced) {
                    log(config, "debug", "MULTILINE_WILDCARD_HIT", from);
                }

                continue; // próxima regra
            }

            // --- LÓGICA WILDCARD LINHA-ÚNICA (...) ---
            if (from.contains("...")) {
                String[] parts = from.split("\\.\\.\\.", -1);
                String startToken = parts.length > 0 ? parts[0] : "";
                String endToken = parts.length > 1 ? parts[1] : "";

                String[] lines = modified.split("\n", -1);
                boolean lineModified = false;

                for (int li = 0; li < lines.length; li++) {
                    String l = lines[li];
                    if (l == null) continue;

                    String searchLine = config.caseSensitive ? l : l.toLowerCase();
                    String sTokenSearch = config.caseSensitive ? startToken : startToken.toLowerCase();
                    String eTokenSearch = config.caseSensitive ? endToken : endToken.toLowerCase();

                    int s;
                    int e;

                    // 1. Achar Inicio
                    if (!sTokenSearch.isEmpty()) {
                        s = searchLine.indexOf(sTokenSearch);
                        if (s == -1) continue; // não encontrou nessa linha
                    } else {
                        s = 0;
                    }

                    // 2. Achar Fim (na mesma linha, buscando a partir do fim do inicio)
                    if (!eTokenSearch.isEmpty()) {
                        int offset = s + sTokenSearch.length();
                        int relativeE = searchLine.indexOf(eTokenSearch, offset);
                        if (relativeE == -1) continue; // fim não encontrado na linha
                        e = relativeE + eTokenSearch.length();
                    } else {
                        e = l.length();
                    }

                    // 3. Substituir (index baseado na linha original)
                    if (s < e) {
                        lines[li] = l.substring(0, s) + to + l.substring(e);
                        changes++;
                        lineModified = true;
                    }
                }

                if (lineModified) {
                    modified = String.join("\n", lines);
                    log(config, "debug", "SINGLELINE_WILDCARD_HIT", from);
                }

                continue; // próxima regra
            }

            // --- REPLACE PADRÃO ---
            if (config.caseSensitive) {
                if (modified.contains(from)) {
                    modified = modified.replace(from, to);
                }
            } else {
                modified = replaceIgnoreCase(modified, from, to);
            }

            if (!before.equals(modified)) {
                changes++;
                log(config, "debug", "REPLACE_HIT", from);
            }
        }

        // --- LÓGICA COMENTÁRIOS ---
        if (config.commentLinesWith != null) {
            for (String keyword : config.commentLinesWith) {
                if (keyword == null) continue;
                String[] lines = modified.split("\n", -1);
                for (int j = 0; j < lines.length; j++) {
                    String l = lines[j];
                    boolean match = config.caseSensitive ? l.contains(keyword) : l.toLowerCase().contains(keyword.toLowerCase());
                    if (match && !l.trim().startsWith("//")) {
                        lines[j] = "//" + l;
                        changes++;
                    }
                }
                modified = String.join("\n", lines);
            }
        }

        // --- UNCOMMENT ---
        if (config.uncommentLinesWith != null) {
            for (String keyword : config.uncommentLinesWith) {
                if (keyword == null) continue;
                String[] lines = modified.split("\n", -1);
                for (int j = 0; j < lines.length; j++) {
                    String l = lines[j];
                    boolean match = config.caseSensitive ? l.contains(keyword) : l.toLowerCase().contains(keyword.toLowerCase());
                    if (match) {
                        lines[j] = l.replaceFirst("^\\s*//", "");
                        changes++;
                    }
                }
                modified = String.join("\n", lines);
            }
        }

        // --- FINALIZAÇÃO E EXPORTAÇÃO ---
        if (!modified.equals(original)) {
            if (config.dryRun) {
                log(config, "normal", "DRY_RUN", file.getName(), changes);
                if (config.previewLines) {
                    previewDiff(config, file.getName(), original, modified);
                }
            } else {
                if (config.createBackup) {
    try {
        File bak = new File(file.getAbsolutePath() + ".bak");
        FileLoader.exportTextToFile(original, bak);
        log(config, "debug", "BACKUP_CREATED", bak.getName());
    } catch (Exception be) {
        log(config, "warn", "BACKUP_FAILED", file.getName());
    }
}
                FileLoader.exportTextToFile(modified, file);
                log(config, "normal", "DELTA_APPLIED", file.getName(), changes);
            }
        } else {
            log(config, "debug", "NO_CHANGES", file.getName());
        }

        if (config.format && !config.dryRun) {
            modified = supremeFormat(modified);
            FileLoader.exportTextToFile(modified, file);
            log(config, "debug", "FORMAT", file.getName());
        }

    } catch (Exception e) {
        Terminal.log(e);
    }
}



// Método auxiliar robusto para substituição Case-Insensitive
public static String replaceIgnoreCase(String text, String search, String replacement) {
    if (text == null || search == null || search.isEmpty()) return text;
    if (replacement == null) replacement = "";

    String lowerText = text.toLowerCase();
    String lowerSearch = search.toLowerCase();
    int start = 0;
    int index = lowerText.indexOf(lowerSearch, start);

    if (index == -1) return text;

    StringBuilder result = new StringBuilder();
    while (index != -1) {
        result.append(text, start, index);
        result.append(replacement);
        start = index + search.length();
        index = lowerText.indexOf(lowerSearch, start);
    }

    result.append(text.substring(start));
    return result.toString();
}

  // ----------------- PARSE DELTA (versão melhorada: boolean parsing seguro e ensure exclude) -----------------
public static DeltaConfig parseDelta(File deltaFile) {
    DeltaConfig config = new DeltaConfig();
    if (deltaFile == null || !deltaFile.exists()) return config;
    List<String> separators = Arrays.asList(">>", "=>", ":::", "::", ",");

    try {
        String text = FileLoader.loadTextFromFile(deltaFile);
        String[] lines = text.split("\n");
        for (int i = 0; i < lines.length; i++) {
            String raw = lines[i];
            if (raw == null) continue;
            // Remove comentários inline (após # ou //)
            String line = raw.split("(#|//)", 2)[0].trim();
            if (line.isEmpty()) continue;
            String cmd = line.toLowerCase();

            // logs:
            if (cmd.startsWith("logs:")) {
                config.logs = cleanValue(line.substring("logs:".length())).toLowerCase();
                continue;
            }

            // logprefix:
            if (cmd.startsWith("logprefix:")) {
                String p = cleanValue(line.substring("logprefix:".length()));
                if (!p.isEmpty()) config.logPrefix = p;
                continue;
            }

            // target:
            if (cmd.startsWith("target:")) {
                String[] split = line.substring("target:".length()).split(",");
                for (String t : split) {
                    String v = cleanValue(t);
                    if (!v.isEmpty()) {
                        config.targets.add(v);
                        log(config, "debug", "TARGET_ADDED", v);
                    }
                }
                continue;
            }

            // exclude:
            if (cmd.startsWith("exclude:")) {
                String[] split = line.substring("exclude:".length()).split(",");
                for (String e : split) {
                    String v = cleanValue(e);
                    if (!v.isEmpty()) {
                        config.excludes.add(v);
                        log(config, "debug", "EXCLUDE_ADDED", v);
                    }
                }
                continue;
            }

            // onlyifcontains:
            if (cmd.startsWith("onlyifcontains:")) {
                config.onlyIfContains = cleanValue(line.substring("onlyifcontains:".length()));
                log(config, "debug", "ONLYIF_SET", config.onlyIfContains);
                continue;
            }

            // dryrun:
            if (cmd.startsWith("dryrun:")) {
                config.dryRun = cleanValue(line.substring("dryrun:".length())).equalsIgnoreCase("true");
                continue;
            }

            // previewlines:
            if (cmd.startsWith("previewlines:")) {
                config.previewLines = cleanValue(line.substring("previewlines:".length())).equalsIgnoreCase("true");
                continue;
            }

            // logslang:
            if (cmd.startsWith("logslang:")) {
                config.logsLang = cleanValue(line.substring("logslang:".length()));
                continue;
            }


// backup:
if (cmd.startsWith("backup:")) {
    config.createBackup = cleanValue(line.substring("backup:".length()))
            .equalsIgnoreCase("true");
    continue;
}
            // format:
            if (cmd.startsWith("format:")) {
                config.format = cleanValue(line.substring("format:".length())).equalsIgnoreCase("true");
                continue;
            }

            // auto:
            if (cmd.startsWith("auto:")) {
                config.auto = cleanValue(line.substring("auto:".length())).equalsIgnoreCase("true");
                continue;
            }

            // casesensitive:
            if (cmd.startsWith("casesensitive:")) {
                config.caseSensitive = cleanValue(line.substring("casesensitive:".length())).equalsIgnoreCase("true");
                continue;
            }

            // commentlineswith:
            if (cmd.startsWith("commentlineswith:")) {
                String[] split = line.substring("commentlineswith:".length()).split(",");
                for (String s : split) {
                    String keyword = cleanValue(s);
                    if (keyword != null && !keyword.isEmpty()) {
                        config.commentLinesWith.add(keyword);
                        log(config, "debug", "COMMENT_ADDED", keyword);
                    }
                }
                continue;
            }

            // uncommentlineswith:
            if (cmd.startsWith("uncommentlineswith:")) {
                String[] split = line.substring("uncommentlineswith:".length()).split(",");
                for (String s : split) {
                    String keyword = cleanValue(s);
                    if (keyword != null && !keyword.isEmpty()) {
                        config.uncommentLinesWith.add(keyword);
                        log(config, "debug", "UNCOMMENT_ADDED", keyword);
                    }
                }
                continue;
            }

            // remove:
            if (cmd.startsWith("remove:")) {
                String pattern = cleanValue(line.substring("remove:".length()));
                if (pattern != null && !pattern.isEmpty()) {
                    DeltaReplaceRule rule = new DeltaReplaceRule();
                    rule.from = pattern;
                    rule.to = "";
                    config.rules.add(rule);
                    log(config, "debug", "REMOVE_RULE", pattern);
                }
                continue;
            }

            // replace:
            if (cmd.startsWith("replace:")) {
                String body = line.substring("replace:".length()).trim();
                String from = null;
                String to = null;
                Matcher m = Pattern.compile("\"((?:[^\"\\\\]|\\\\.)*)\"\\s+\"((?:[^\"\\\\]|\\\\.)*)\"").matcher(body);
                if (m.find()) {
                    from = m.group(1).replace("\\\"", "\"");
                    to = m.group(2).replace("\\\"", "\"");
                }

                if (from == null) {
                    for (String sep : separators) {
                        if (body.contains(sep)) {
                            String[] p = body.split(Pattern.quote(sep), 2);
                            if (p.length == 2) {
                                from = cleanValue(p[0]);
                                to = cleanValue(p[1]);
                            }
                            break;
                        }
                    }
                }

                if (from == null) {
                    from = cleanValue(body);
                    to = "";
                }

                if (from == null || from.isEmpty()) {
                    log(config, "warn", "REPLACE_INVALID", (i + 1));
                    continue;
                }

                if (to == null) to = "";
                DeltaReplaceRule rule = new DeltaReplaceRule();
                rule.from = from;
                rule.to = to;
                config.rules.add(rule);
                log(config, "debug", "REPLACE_ADDED", from, to);
                continue;
            }

            log(config, "debug", "LINE_IGNORED", line);
        }

    } catch (Exception e) {
        Terminal.log(e);
    }

    // Garantir exclude interno mesmo que usuário remova do arquivo
    if (!config.excludes.contains("DeltaSys/*")) {
        config.excludes.add(0, "DeltaSys/*");
    }

    log(config, "normal", "PARSE_COMPLETE", config.rules.size());
    return config;
}

  // ---------------- LIMPA VALORES (aspas e colchetes)
  static String cleanValue(String v) {
    if (v == null) return null;
    v = v.trim();
    if (v.startsWith("\"") && v.endsWith("\"") && v.length() >= 2) v = v.substring(1, v.length() - 1);
    if (v.startsWith("[") && v.endsWith("]") && v.length() >= 2) v = v.substring(1, v.length() - 1);
    return v.trim();
  }

  static void log(DeltaConfig config, String level, String key, Object... args) {
    if (config == null) return;
    // modo de log
    String mode = config.logs;
    if (mode == null) mode = "normal";
    if (mode.equals("none")) return;
    if (mode.equals("normal") && level.equals("debug")) return;
    // prefixo
    String prefix = config.logPrefix;
    if (prefix == null || prefix.isEmpty()) prefix = "[DELTA]";
    // seleciona idioma
    String lang = config.logsLang;
    if (lang == null || !LanguageManager.translations.containsKey(lang)) lang = "ptbr";
    Map<String, String> dict = LanguageManager.translations.get(lang);
    String template = dict.getOrDefault(key, key);
    // formata a mensagem
    String message;
    try {
      message = String.format(template, args);
    } catch (Exception e) {

      message = template + " " + Arrays.toString(args); // fallback se der erro
    }

    Terminal.log(prefix + " " + message);
  }

  static void previewDiff(DeltaConfig config, String fileName, String original, String modified) {
    if (original == null || modified == null) return;
    String[] a = original.split("\n", -1);
    String[] b = modified.split("\n", -1);
    int max = Math.max(a.length, b.length);
    int changes = 0;
    log(config, "normal", "PREVIEW_FILE", fileName);
    for (int i = 0; i < max; i++) {
      String la = i < a.length ? a[i] : "";
      String lb = i < b.length ? b[i] : "";
      if (!la.equals(lb)) {
        changes++;
        String lineNumber = String.valueOf(i + 1);
        if (!la.isEmpty()) {
          Terminal.log(" " + lineNumber + " | - " + la);
        }

        if (!lb.isEmpty()) {
          Terminal.log(" " + lineNumber + " | + " + lb);
        }

        Terminal.log(""); // espaço visual
      }
    }
    if (changes == 0) {
      log(config, "normal", "PREVIEW_NO_CHANGES", fileName);
    } else {

      log(config, "normal", "PREVIEW_CHANGES_TOTAL", changes);
    }
  }
}