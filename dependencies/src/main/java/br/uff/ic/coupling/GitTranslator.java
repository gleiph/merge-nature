    private static String RENAMEFROM = "rename from";
    private static String RENAMETO = "rename to";
        } else if (delta.contains("\n")) {
                initialline = finalLine(line); //initialLine
                iteratorRemove++;

            } else if (isRemovedLine(line)) {
                Operation operation = getOperation(line, OperationType.REMOVE, iteratorRemove);
                result.add(operation);
                //iteratorRemove++;

            } else {

//                iteratorAdd++;
                iteratorRemove++;

            }
        }

        return result;
    }

    public List<Operation> translateDeltaBase(String delta) {
        //-----------------------------------------------
        //Variables used in the method
        //-----------------------------------------------
        String filename = "";
        List<Operation> result = new ArrayList<>();
        int initialline, iteratorRemove = 0;
        boolean newFile = false;

        //-----------------------------------------------
        //Breaking the input string (delta) into lines
        //-----------------------------------------------
        String OS = System.getProperty("os.name");

        String[] deltaLines = null;

        if (delta.contains(", ")) {
            deltaLines = delta.split(", ");
        } else if (delta.contains("\n")) {
            deltaLines = delta.split("\n");
        }

        //--------------------------------------------------------
        //Reading the delta content to transform into OO structure
        //--------------------------------------------------------
        for (String line : deltaLines) {

            //--------------------------------------------------------
            //Removing whitespace in the begin of the line
            //--------------------------------------------------------
//            line = removingWhitespace(line);//work it better
            if (line.startsWith(LINEINTERVAL)) {
                //read the interval line
                initialline = initialLine(line);
                if (initialline == 0) {//it is a added file or renamed file
                    initialline = 1;
                    newFile = true;
                }
//                iteratorAdd = initialline;
                iteratorRemove = initialline;

            } else if (isAddedLine(line) && (newFile)) {
                Operation operation = getOperation(line, OperationType.ADD, iteratorRemove);
                result.add(operation);
//                iteratorAdd++;
                iteratorRemove++;

            } else if (isAddedLine(line) && (!newFile)) {
                Operation operation = getOperation(line, OperationType.ADD, iteratorRemove);
                result.add(operation);
//                iteratorAdd++;
    public List<String> translateRenamed(String delta) {
        //-----------------------------------------------
        //Variables used in the method
        //-----------------------------------------------
        String filename = "";
        String oldfilename = "";
        String newfilename = "";
        List <String> result = new ArrayList<>();

        //-----------------------------------------------
        //Breaking the input string (delta) into lines
        //-----------------------------------------------
        String OS = System.getProperty("os.name");

        String[] deltaLines = null;

        if (delta.contains(", ")) {
            deltaLines = delta.split(", ");
        } else if (delta.contains("\n")) {
            deltaLines = delta.split("\n");
        }

        //--------------------------------------------------------
        //Reading the delta content to transform into OO structure
        //--------------------------------------------------------
        for (String line : deltaLines) {
            if (line.startsWith(RENAMEFROM)) {
                oldfilename = line.substring(line.indexOf("from") + 5, line.length());
                result.add(oldfilename);
            } else if (line.startsWith(RENAMETO)) {
                newfilename = line.substring(line.indexOf("to") + 3, line.length());
                result.add(newfilename);
            }
        }

        return result;
    }

    private int finalLine(String line) {

        //@@ -10,8 10,13 @@ package metodo_atributo;
        String[] intervals = line.split("\\+");

        //intervals[1] = intervals[1].replaceFirst("+", "");
        String[] limits = intervals[1].split(",");

        return Integer.parseInt(limits[0].replace(" ", ""));
    }

