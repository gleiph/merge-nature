/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.github.github.parser;

import br.uff.ic.github.github.CMD;
import br.uff.ic.github.github.CMDOutput;
import br.uff.ic.github.github.data.Language;
import br.uff.ic.github.github.data.Project;
import br.uff.ic.github.github.file.WriteFile;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author Gleiph
 */
public class GithubAPI {

    private static String base = null;
    private static final String BEGIN_PARENTS = "\"parents\": [";
    private static final String END_PARENTS = "]";

    public static void init(String login, String password) {
        base = "curl -i -u " + login + ":" + password + " ";
    }

    /**
     * Returns the number of commits from GitHub project
     *
     * @param url from a Github project
     * @return Number of commits
     */
    public static int commits(String url) {
        int result = 0;
        int controller = 0;

        if (url.endsWith("/")) {
            url += "commits";
        } else {
            url += "/commits";
        }

        CMDOutput output = null;
        String link = url;

        while (link != null) {

            output = CMD.cmdGithub(base + link);

            for (String line : output.getOutput()) {
                if (line.contains("{")) {
                    controller++;
                }
                if (line.contains("}")) {
                    controller--;

                    if (controller == 0) {
                        result++;
                    }
                }
            }

            link = Parser.getLink(output.getOutput());
        }

        return result;
    }

    /**
     * Returns the number of merges from GitHub project
     *
     * @param url from a Github project
     * @return Number of commits
     */
    public static int merges(String url) {
        int result = 0;
        boolean parents_area = false;
        int parents_number = 0;

        if (url.endsWith("/")) {
            url += "commits";
        } else {
            url += "/commits";
        }

        CMDOutput output = null;
        String link = url;

        while (link != null) {

            output = CMD.cmdGithub(base + link);

            for (String line : output.getOutput()) {
                if (line.contains(BEGIN_PARENTS)) {
                    parents_area = true;
                }

                if (parents_area) {
                    if (line.contains("{")) {
                        parents_number++;
                    }

                    if (line.contains(END_PARENTS)) {
                        if (parents_number > 1) {
                            result++;
                        }
                        parents_area = false;
                        parents_number = 0;
                    }
                }
            }

            link = Parser.getLink(output.getOutput());
        }

        return result;
    }

    public static int languages(String url) {
        int result = 0;
        boolean area = false;

        if (url.endsWith("/")) {
            url += "languages";
        } else {
            url += "/languages";
        }

        CMDOutput output = CMD.cmdGithub(base + url);

        for (String line : output.getOutput()) {

            if (line.contains("{")) {
                area = true;
            } else if (line.contains("}")) {
                area = false;
            } else if (area) {
                result++;
            }

        }

        return result;
    }

    public static void generic(String query) {

        CMDOutput output = CMD.cmdGithub(base + query);

        for (String line : output.getOutput()) {
            System.out.println(line);
        }

    }

    public static List<Language> languagesList(String url) {

        List<Language> result = new ArrayList<Language>();
        String name = null;
        int size = 0;
        double total = 0;

        boolean area = false;

//        if (url.endsWith("/")) {
//            url += "languages";
//        } else {
//            url += "/languages";
//        }
        CMDOutput output = CMD.cmdGithub(base + url);

        for (String line : output.getOutput()) {

            if (line.contains("{")) {
                area = true;
            } else if (line.contains("}")) {
                area = false;
            } else if (area) {
                if (!line.contains(":")) {
                    continue;
                }
                if (line.contains("\"message\": \"Repository access blocked\"")) {
                    break;
                }
                String[] split = line.split("\"");
                name = split[1];

                split = line.split(":");

                size = Integer.parseInt(split[1].replaceAll(" ", "").replaceAll(",", ""));
                total += size;
                result.add(new Language(name, size));
            }

        }

        for (Language language : result) {
            language.setPercentage(100.00 * language.getSize() / total);
        }

        return result;
    }

    public static int contributors(String url) {
        int result = 0;
        int controller = 0;

//        if (url.endsWith("/")) {
//            url += "contributors";
//        } else {
//            url += "/contributors";
//        }
        CMDOutput output = null;
        String link = url;

        while (link != null) {

            output = CMD.cmdGithub(base + link);

            for (String line : output.getOutput()) {
                if (line.contains("{")) {
                    controller++;
                }
                if (line.contains("}")) {
                    controller--;

                    if (controller == 0) {
                        result++;
                    }
                }
            }

            link = Parser.getLink(output.getOutput());
        }

        return result;
    }

    public static void projects(int since, String reportPath) {

        WriteFile fw;
        File report = new File(reportPath);

        if (report.isFile()) {
            fw = new WriteFile(reportPath, false);
            //TODO: read the file and continue
        } else {
            fw = new WriteFile(reportPath, true);
        }

        try {
            fw.open();
        } catch (IOException ex) {
            Logger.getLogger(GithubAPI.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Problem during report generation!");
            return;
        }

        int cont = 0;

        String LINK = "Link:";

        CMDOutput output = null;
        String link = "https://api.github.com/repositories";

        if (since > 0) {
            link = "https://api.github.com/repositories?since=" + since;
        }

        JSONParser parser = new JSONParser();

        while (link != null) {

            output = CMD.cmdGithub(base + link);

            String content = "";
            boolean begin = false;
            int count = 0;

            for (String line : output.getOutput()) {

                if (line.equals("[")) {
                    begin = true;
                }

                if (line.endsWith("},")) {
                    count++;
                }

                if (begin && !line.equals("[") && !line.equals("]")) {

                    if (line.endsWith("},")/* && count == 2*/) {
                        content += line.replace("},", "}");
                    } else {
                        content += line;
                    }
                }

                if (count == 2) {

                    try {
                        Object parse = parser.parse(content);
                        JSONObject jsono = (JSONObject) parse;

                        String name = jsono.get("name").toString();
                        String fullName = jsono.get("full_name").toString();
                        String url = jsono.get("url").toString();
                        String htmlUrl = jsono.get("html_url").toString();
                        String contributorsUrl = jsono.get("contributors_url").toString();
                        String languagesUrl = jsono.get("languages_url").toString();
                        int contributors = GithubAPI.contributors(contributorsUrl);
                        List<Language> languagesList = GithubAPI.languagesList(languagesUrl);

                        System.out.println(name);
//                        System.out.println("\tFullName: "+fullName);
//                        System.out.println("\tURL: "+url);
//                        System.out.println("\tHtmlURL: "+htmlUrl);
//                        System.out.println("\tContributors: " + contributors);
//                        System.out.println("\tLanguages: " + languagesList.size());
//                        for (Language language : languagesList) {
//                            System.out.println("\t\t"+language.getName()+": "+language.getPercentage());
//                        }

                        fw.writeln("Name: " + name);
                        fw.writeln("\tFullName: " + fullName);
                        fw.writeln("\tURL: " + url);
                        fw.writeln("\tHtmlURL: " + htmlUrl);
                        fw.writeln("\tContributors: " + contributors);
                        fw.writeln("\tLanguages: " + languagesList.size());
                        for (Language language : languagesList) {
                            fw.writeln("\t\t" + language.getName() + ": " + language.getPercentage());
                        }

                        content = "";
                        count = 0;
                    } catch (ParseException ex) {
                        Logger.getLogger(GithubAPI.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

            }

            link = Parser.getLink(output.getOutput());
            String[] split = link.split("=");
            System.out.println("Next:" + split[split.length - 1]);
            fw.writeln("Next:" + split[split.length - 1]);
            cont = 0;
        }

        fw.close();
    }

    public static Project projectInfo(String url) {

        String CREATED_AT = "\"created_at\":";
        String UPDATED_AT = "\"updated_at\":";
        String NAME = "\"name\":";
        String URL = "\"url\":";
        String HTML_URL = "\"html_url\":";

        Project result = new Project();

        CMDOutput output = CMD.cmdGithub(base + url);

        for (String line : output.getOutput()) {

            if (line.contains(CREATED_AT)) {
                result.setCreatedAt(Parser.getContent(line));
            } else if (line.contains(UPDATED_AT)) {
                result.setUpdatedAt(Parser.getContent(line));
            } else if (line.contains(NAME)) {
                result.setName(Parser.getContent(line));
            } else if (line.contains(URL)) {
                result.setUrl(Parser.getContent(line));
            } else if (line.contains(HTML_URL)) {
                result.setHtmlUrl(Parser.getContent(line));
            }
        }

        return result;
    }

}
