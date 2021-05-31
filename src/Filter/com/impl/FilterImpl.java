package Filter.com.impl;

import Filter.com.interfeces.Filter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public  class  FilterImpl implements Filter {
    // if date u wanna change u_r date_format,u have the setter in class
    // But it static information should be in some isolate place
    SimpleDateFormat simpleDateFormat;
    List<String> input;
    ArrayList<String[]> listOfQuery;
    ArrayList<String[]> listOfWaitingTimeLines;


    public FilterImpl(List<String> input) {
        listOfQuery = new ArrayList<>();
        listOfWaitingTimeLines = new ArrayList<>();
        this.input = input;
        String stringDateFormat = "dd.M.yyyy";
        simpleDateFormat = new SimpleDateFormat(stringDateFormat);
        init(input,listOfWaitingTimeLines,listOfQuery);
    }

    public List<String> outResultMethod(){
        return checkTheQueryWork(listOfWaitingTimeLines,listOfQuery);
    }

    private List<String> checkTheQueryWork(List<String[]> listOfWaitingTimeLines, List<String[]> listOfQuery) {
        List<String> result = new ArrayList<>();
        List<Integer> tempList = new ArrayList<>();

        int cycleCounter=0;
        while (!listOfQuery.isEmpty() && listOfQuery.size()!=cycleCounter){
            String[] checkingStringArrayElement = listOfQuery.get(cycleCounter);
            String[] dateRage = checkingStringArrayElement[4].split("-");
            Iterator<String> iterator = Arrays.stream(dateRage).iterator();
            iterator.next();
            String data = dateRage[0];
            if (iterator.hasNext()){
                data = iterator.next();
            }
            for (String[] compareElement:listOfWaitingTimeLines){
                if (isNumberInRange(checkingStringArrayElement[1], compareElement[1]) &&
                        isNumberInRange(checkingStringArrayElement[2], compareElement[2]) &&
                        isWithinDateInRange(getDataFromString(compareElement[4],
                                getSimpleDateFormat()), getDataFromString(dateRage[0],
                                getSimpleDateFormat()), getDataFromString(data, getSimpleDateFormat()))){
                    tempList.add(Integer.parseInt(compareElement[5]));
                }
            }
            int tempIntCumulativeCounter = 0;
            if (!tempList.isEmpty()){
                for (Integer number:tempList) {
                    tempIntCumulativeCounter+=number;
                }
                result.add( String.valueOf(tempIntCumulativeCounter/tempList.size()));
            }else {
                result.add("-");
            }
            tempList.clear();
            cycleCounter++;
        }
        return result;
    }


    private Date getDataFromString(String s, DateFormat dateFormat) {
        Date date = null;
        try {
            date = dateFormat.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();

        }
        return date;
    }


    private boolean isNumberInRange(String checkStringNumber, String compareStringNumber) {
        boolean inRange = false;
        if (checkStringNumber.equals("*") || compareStringNumber.equals("*")){
            inRange = true;
        }else {
            String[] checkString = checkStringNumber.split("\\.");
            String[] compareString = compareStringNumber.split("\\.");
            Iterator<String> checkStringIterator = Arrays.stream(checkString).iterator();
            Iterator<String> compareStringIterator = Arrays.stream(compareString).iterator();

            while (checkStringIterator.hasNext()){
                String nextCompareString = null;
                String nextCheckString = checkStringIterator.next();
                if (compareStringIterator.hasNext()){
                    nextCompareString = compareStringIterator.next();
                }
                inRange = nextCheckString.equals(nextCompareString);
            }
        }
        return inRange;
    }


    private boolean isWithinDateInRange(Date date, Date before, Date after) {
        return  (date.after(before)) && (date.before(after));
    }

    private void init(List<String> input,List<String[]> listOfWaitingTimeLines ,List<String[]> listOfQuery){

        String pattern = "(^C)";
        Pattern ptrn = Pattern.compile(pattern);
        for (int i = 1; i < input.size(); i++) {
            Matcher matcher = ptrn.matcher(input.get(i));
            if (matcher.find()) {
                String[] s1 = input.get(i).split("\\s+");
                listOfWaitingTimeLines.add(s1);
            }else {
                String[] s1 = input.get(i).split("\\s+");
                listOfQuery.add(s1);
            }
        }
    }


    public SimpleDateFormat getSimpleDateFormat() {
        return simpleDateFormat;
    }

    public void setSimpleDateFormat(SimpleDateFormat simpleDateFormat) {
        this.simpleDateFormat = simpleDateFormat;
    }
}
