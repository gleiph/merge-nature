/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.gems.resources.github.cmd;

import br.uff.ic.gems.resources.cmd.CMDOutput;
import br.uff.ic.gems.resources.github.authentication.User;
import br.uff.ic.gems.resources.github.parser.GithubAPI;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author gleiph
 */
public class CMDGithub {
    
    public static CMDOutput cmdGithub(String command) {

        CMDOutput result = new CMDOutput();
        boolean okay = false;

        while (!okay) {
            okay = true;

            try {

                Process exec;

                exec = Runtime.getRuntime().exec(command);

                String s;

                BufferedReader stdInput = new BufferedReader(new InputStreamReader(exec.getInputStream()));

                BufferedReader stdError = new BufferedReader(new InputStreamReader(exec.getErrorStream()));

                // read the output from the command
                while ((s = stdInput.readLine()) != null) {
                    result.addOutput(s);
                    if (s.contains("X-RateLimit-Remaining: 0") || s.contains("API rate limit exceeded")) {
                        result = new CMDOutput();
                        okay = false;
                        try {
                            Thread.sleep(10000);
                            System.out.println("Waiting...");

                            //curl -i -u maparao:fake1234 https://api.github.com/repositories?since=5532
                            String[] split = command.split(" ");

                            // To do: Manage users 
                            User nextUser = GithubAPI.nextUser();

                            if (split.length > 3) {
                                split[3] = nextUser.getLogin() + ":" + nextUser.getPassword();
                            }

                            command = "";

                            for (String piece : split) {
                                command += piece + " ";
                            }

                        } catch (InterruptedException ex) {
                            Logger.getLogger(CMDGithub.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }

//                    if (s.contains("\"Not Found\"")) {
//                        return new CMDOutput();
//                    }
                }

                // read any errors from the attempted command
                while ((s = stdError.readLine()) != null) {
                    result.addErrors(s);
                }

            } catch (IOException ex) {
                Logger.getLogger(CMDGithub.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }
}
