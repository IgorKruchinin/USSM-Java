package USSM.USSM.LOQ;

import USSM.USM.*;

import java.util.*;

public class LOQ {
    private Map<String, USM> profiles;
    private Stack<Integer> integers;
    private Stack<String> strings;
    private String prof_name = "";
    private int lastFormat = -1;
    public LOQ() {
        profiles = new HashMap<>();
        for (USM prof: USM.get_profiles()) {
            profiles.put(prof.get_name(), prof);
        }
        integers = new Stack<>();
        strings = new Stack<>();
    }
    public void parseQuery(String query) throws LOQNoProfileException {
        boolean create_flag = false;
        boolean add_flag = false;
        boolean post_add_flag = false;
        boolean adding_flag = false;
        boolean entry_flag = false;
        boolean write_flag = false;
        boolean name_write_flag = false;
        boolean writing_flag = false;
        boolean get_flag = false;
        boolean name_get_flag = false;
        boolean getting_flag = false;
        boolean index_get_flag = false;
        boolean add_value_flag = false;
        boolean adding_value_flag = false;
        int index = -1;
        //String prof_name = "";
        String name = "";
        int format = -1;
        for (String q: query.split(" ")) {
            if (create_flag) {
                create_flag = false;
                if (prof_name.equals("")) {
                    profiles.put(q, new USM(q));
                    prof_name = q;
                } else {
                    add_flag = true;
                }
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
                        name = "";
                        break;
                    case 1:
                        profiles.get(prof_name).create_ssec(name);
                        profiles.get(prof_name).to_file();
                        name = "";
                        break;
                }
                continue;
            } else if (entry_flag) {
                entry_flag = false;
                if (profiles.containsKey(q)) {
                    prof_name = q;
                } else {
                    throw new LOQNoProfileException("No profile named " + q);
                }
                continue;
            } else if (write_flag) {
                write_flag = false;
                switch(q) {
                    case "int":
                        format = 0;
                        break;
                    case "str":
                        format = 1;
                        break;
                }
                name_write_flag = true;
                continue;
            } else if (name_write_flag) {
                name_write_flag = false;
                name = q;
                writing_flag = true;
                continue;
            } else if (writing_flag) {
                writing_flag = false;
                switch(format) {
                    case 0:
                        profiles.get(prof_name).geti(name).add(Integer.parseInt(q));
                        profiles.get(prof_name).to_file();
                        break;
                    case 1:
                        profiles.get(prof_name).gets(name).add(q);
                        profiles.get(prof_name).to_file();
                        break;
                }
                continue;
            } else if (get_flag) {
                get_flag = false;
                switch(q) {
                    case "int":
                        format = 0;
                        break;
                    case "str":
                        format = 1;
                        break;
                }
                name_get_flag = true;
                continue;
            } else if (name_get_flag) {
                name_get_flag = false;
                name = q;
                index_get_flag = true;
                continue;
            } else if (index_get_flag) {
                index_get_flag = false;
                index = Integer.parseUnsignedInt(q);
                getting_flag = true;
                continue;
            } else if (getting_flag) {
                getting_flag = false;
                switch (format) {
                    case 0:
                        lastFormat = 0;
                        integers.push(profiles.get(prof_name).geti(name).get(index));
                        break;
                    case 1:
                        lastFormat = 1;
                        strings.push(profiles.get(prof_name).gets(name).get(index));
                        break;
                }
                continue;
            } else if (add_value_flag) {
                add_value_flag = false;
                name = q;
                adding_value_flag = true;
                continue;
            } else if (adding_value_flag) {
                adding_value_flag = false;
                if (profiles.get(prof_name).get(name) instanceof IntSection) {
                    profiles.get(prof_name).geti(name).add(Integer.parseInt(q));
                } else if (profiles.get(prof_name).get(name) instanceof StringSection) {
                    profiles.get(prof_name).gets(name).add(q);
                }
                continue;
            }
            switch(q) {
                case "create":
                    create_flag = true;
                    break;
                case "add":
                    add_value_flag = true;
                    break;
                case "entry":
                    entry_flag = true;
                    break;
                case "write":
                    write_flag = true;
                    break;
                case "get":
                    get_flag = true;
                    break;
                case "table":
                    AsTable(profiles.get(prof_name));
                    break;
                case "exit":
                    prof_name = "";
                    break;
                case "quit":
                    System.exit(0);
            }
        }
    }
    int popInt() {
        return integers.pop();
    }
    String popStr() {
        return strings.pop();
    }
    int getLastFormat() {
        return lastFormat;
    }
    String getProfName() {
        return prof_name;
    }
    private List<String> AsTable(USM profile) {
        List<String> secLst = new Vector<>();
        for (Section s: profile.getAll()) {
            if (s instanceof StringSection) {
                for (int i = 0; i < s.size(); ++i) {
                    secLst.add(((StringSection)s).get(i));
                }
            } else if (s instanceof IntSection) {
                for (int i = 0; i < s.size(); ++i) {
                    secLst.add(String.valueOf(((IntSection)s).get(i)));
                }
            }
        }
        return secLst;
    }
}
