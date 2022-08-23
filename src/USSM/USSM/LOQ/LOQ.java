package USSM.USSM.LOQ;
import USM.USM;

import java.util.Map;

public class LOQ {
    Map<String, USM> profiles;
    public void parseQuery(String query) throws LOQNoProfileException {
        boolean create_flag = false;
        boolean add_flag = false;
        boolean post_add_flag = false;
        boolean adding_flag = false;
        boolean entry_flag = false;
        String prof_name = "";
        String name = "";
        int format = -1;
        for (String q: query.split(" ")) {
            if (create_flag) {
                create_flag = false;
                profiles.put(q, new USM(q));
                prof_name = q;
                continue;
            } else if (add_flag) {
                add_flag = false;
                switch(q) {
                    case "int":
                        format = 0;
                        break;
                    case "str":
                        format = 1;
                        break;
                }
                post_add_flag = true;
                continue;
            } else if (post_add_flag) {
                post_add_flag = false;
                name = q;
                adding_flag = true;
                continue;
            } else if (adding_flag) {
                adding_flag = false;
                switch(format) {
                    case 0:
                        profiles.get(prof_name).create_isec(name);
                        profiles.get(prof_name).to_file();
                        break;
                }
                continue;
            } else if (entry_flag) {
                if (profiles.containsKey(q)) {
                    prof_name = q;
                } else {
                    throw new LOQNoProfileException("No profile named " + q);
                }
                continue;
            }
            switch(q) {
                case "create":
                    create_flag = true;
                    break;
                case "add":
                    add_flag = true;
                    break;
                case "entry":
                    entry_flag = true;
                    break;
                case "write":

            }
        }
    }
    public void setTableMode() {
    }
}
