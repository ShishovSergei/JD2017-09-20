package by.it.malyshev.calc;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Parser {

    static Var fromString(String strInput) {
        Depository.logWrite("Input: "+strInput);
        Var res;
        if (!(strInput.trim().equals("printvar")) &&
                !(strInput.trim().equals("sortvar"))) {


            Pattern p = Pattern.compile("=");
            Matcher m = p.matcher(strInput);
            try {
                if (!m.find()) {
                    throw new CalcError("Нет такой операции");
                }
            } catch (CalcError e) {
                System.out.println(e.getMessage());
                Depository.logWrite(e.getMessage());
                return null;
            }

            String[] strOperands = strInput.trim().split("=");
            if (strOperands[1].trim().equals("null")) return null;

            Pattern p1 = Pattern.compile(Patterns.exAny);
            Matcher m1 = p1.matcher(strOperands[1]);
            try {
                if (!m1.find()) {
                    throw new CalcError("Нет такой операции");
                }
            } catch (CalcError e) {
                System.out.println(e.getMessage());
                Depository.logWrite(e.getMessage());
                return null;
            }
            int counter = 0;
            m1.reset();
            try {
                while (m1.find()) counter++;
                if (counter > 1) {
                    Depository.variablesCollection.put(strOperands[0], singleOperation(strOperands[1]));
                    return singleOperation(strOperands[1]);
                } else {
                    Depository.variablesCollection.put(strOperands[0], selectTypeOfOperand(strOperands[1]));
                    res= selectTypeOfOperand(strOperands[1]);
//                    Depository.logWrite(strOperands[0]+" = " + selectTypeOfOperand(strOperands[1]));
                    return res;
                }
            } catch (ArithmeticException e) {
                System.out.println(e.getMessage());
                Depository.logWrite(e.getMessage());
                return null;
            }


        } else if (strInput.trim().equals("printvar")) Depository.printVar();
        else if (strInput.trim().equals("sortvar")) Depository.sortVar();
        return null;
    }


    static Var singleOperation(String strSingleOperation) {
        Var res;
        try {
            Pattern p = Pattern.compile(Patterns.exAny);
            Matcher m = p.matcher(strSingleOperation);
            if (!m.find()) {
                throw new CalcError("Нет такой операции");
            }
            String strOper = strSingleOperation.substring(strSingleOperation.indexOf(m.group())
                    + m.group().length()).trim();

            m.reset();
            int i = 0;
            String[] strOperands = new String[2];
            while (m.find()) {
                strOperands[i++] = m.group();
            }
            Pattern p1 = Pattern.compile(Patterns.exOper);
            Matcher m1 = p1.matcher(strOper);
            if (m1.find()) {
                switch (m1.group()) {
                    case "+":
                        res= selectTypeOfOperand(strOperands[0]).add(selectTypeOfOperand(strOperands[1]));
//                        Depository.logWrite(" Output: "+res.toString()+"\r\n");
                        return res;
                    case "-":
                        if (strOperands[1].trim().charAt(0) == '-') strOperands[1] = strOperands[1].trim().substring(1);
                        res= selectTypeOfOperand(strOperands[0]).sub(selectTypeOfOperand(strOperands[1]));
//                        Depository.logWrite(selectTypeOfOperand(strOperands[0])+" - " + selectTypeOfOperand(strOperands[1])+" = "+res.toString());
//                        Depository.logWrite(" Output: "+res.toString()+"\r\n");
                        return res;
                    case "*":
                        res= selectTypeOfOperand(strOperands[0]).mul(selectTypeOfOperand(strOperands[1]));
//                        Depository.logWrite(selectTypeOfOperand(strOperands[0])+" * " + selectTypeOfOperand(strOperands[1])+" = "+res.toString());
//                        Depository.logWrite(" Output: "+res.toString()+"\r\n");
                        return res;
                    case "/":
                        res= selectTypeOfOperand(strOperands[0]).div(selectTypeOfOperand(strOperands[1]));
//                        Depository.logWrite(selectTypeOfOperand(strOperands[0])+" / " + selectTypeOfOperand(strOperands[1])+" = "+res.toString());
//                        Depository.logWrite(" Output: "+res.toString()+"\r\n");
                        return res;
                }
           } else throw new CalcError("Нет такой операции");
        } catch (CalcError e) {
            System.out.println(e.getMessage());
            Depository.logWrite(e.getMessage());
            return null;
        }
        return null;
    }


    private static Var selectTypeOfOperand(String strOperand) {
        if (strOperand.trim().length() > 2 && strOperand.trim().substring(0, 2).equals("{{"))
            return new VarM(strOperand.trim());
        else if (strOperand.trim().substring(0, 1).equals("{")) return new VarV(strOperand.trim());
        else return new VarD(strOperand.trim());
    }

}
