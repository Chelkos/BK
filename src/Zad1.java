import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Zad1 {
    public static PossibleCodes[] lettersPos;
    public static int currPos = 0;
    public static String[] whatIs;
    public static int specialChar = 15;
    public static int checkShorter(int l1,int l2) {
        if(l1>lettersPos.length && l2>lettersPos.length)
            return lettersPos.length;
        else if(l1<l2)
            return l1;
        else
            return l2;
    }
    public static int checkBinaryValue(int[] a){
        int value=0;
        for(int i=7;i>=0;i--)
        {
            value += a[7-i]*Math.pow(2,i);
        }
        return value;
    }
    public static char changeBinaryToLetter(int[] a){
        char letter = (char) checkBinaryValue(a);
        return letter;
    }
    public static boolean checkIfGivesSpecial(char a){

        if(a=='"' || a=='.' || a==',' || a==' ')
            return true;
        else
            return false;
    }
    public static boolean checkIfGivesSymbol(int[] a){
        int val = checkBinaryValue(a);
        if(val>64 && val<91)
            return true;
        else if(val>96 && val < 123)
            return true;
        else if(val=='"' || val=='.' || val==',' || val==' ')
            return true;
        else
            return false;
    }
    public static boolean checkIfGivesLetter(char a){
        if(a>64 && a<91)
            return true;
        else if(a>96 && a< 123)
            return true;
        else
            return false;
    }
    public static void tryDecoding(int i) {
        HashMap<Character, Integer> values = new HashMap<Character, Integer>();
        ArrayList<Character> possible = new ArrayList<Character>();
        ArrayList<Character> possibleL = new ArrayList<Character>();
        int max = 0;
        char prob='?';
        for (int[] xor : lettersPos[i].codes) {
            int[] xor2 = xorThem(xor, turnStringBytesIntoInts(whatIs[i]));
            char curr = changeBinaryToLetter(xor2);
            if(checkIfGivesSymbol(xor2)) {
                if (values.get(curr) == null) {
                    values.put(curr, 1);
                    possible.add(curr);
                    if(checkIfGivesLetter(curr))
                        possibleL.add(curr);
                } else
                {
                    values.put(curr, values.get(curr) + 1);
                }
            }
        }
        System.out.print("[");
        if(possibleL.size()<=3) {

            for (Character c : possibleL)
            {
                if(max<values.get(c))
                {
                    max = values.get(c);
                    prob = c;
                }

            }
            System.out.print(prob);

        }
        else if(specialChar>0)
        {
            specialChar--;
            for (Character c : possible) {
                if (checkIfGivesSpecial(c))
                    System.out.print(c);
            }

        }
        System.out.print("]");

           // System.out.print(whatIs[i] + " ");
            /*    for(int j=0;j<8;j++)
                    System.out.print(lettersPos[i].code1[j]);
            System.out.print(" ");
              for(int j=0;j<8;j++)
            System.out.print(lettersPos[i].code2[j]);*/
                   }

    public static int checkWithSpace(int[] a,int[] b){
        int[] space = {0,0,1,0,0,0,0,0};

        int[] xored = xorThem(a,b);
       // showIntTab(xored);
        if(xored[1] == 1)
        {
            xored = xorThem(xored,space);
           int[] xoreda = xorThem(xored,a);
            int[] xoredb = xorThem(xored,b);
            if(lettersPos[currPos]==null)
            {
                lettersPos[currPos] = new PossibleCodes();
                lettersPos[currPos].add(xorThem(xored,a));
                lettersPos[currPos].add(xorThem(xored,b));
                return 1;
            }
            else if(!lettersPos[currPos].codes.contains(xoreda))
            {
                lettersPos[currPos].add(xoreda);

            }
            else if(!lettersPos[currPos].codes.contains(xoredb))
            {
                lettersPos[currPos].add(xoredb);

            }
            else
            {
                return 1;
            }

        }

        return 0;
    }
    public static void checkForPossibleCodes(String[] M1,String[] M2){
        int len;
        len = checkShorter(M1.length,M2.length);
            for(int i=0;i<len;i++) {
                int[] a,b;
                currPos = i;
                a = turnStringBytesIntoInts(M1[i]);
                b = turnStringBytesIntoInts(M2[i]);
                checkWithSpace(a,b);

            }
    }
    public static int[] xorThem(int[] c1,int[] c2) {
        int[] xored = new int[8];
        for(int i=0;i<8;i++)
            {
                xored[i]=c1[i]^c2[i];
            }
        return xored;
    }
    public static void checkEveryCombination(ArrayList<String> fileCutToStringList ){
        for(int i=0;i<fileCutToStringList.size();i++)
            for(int j=i+1;j<fileCutToStringList.size();j++)
                checkForPossibleCodes(cutStringToBytes(fileCutToStringList.get(i)),cutStringToBytes(fileCutToStringList.get(j)));
    }
    public static int[] turnStringBytesIntoInts(String oneLineWithBytes){
        int[] byteTab = new int[8];
        for(int i=0;i<8;i++){
                byteTab[i] = Integer.parseInt(String.valueOf(oneLineWithBytes.toCharArray()[i]));
            }
        return byteTab;
    }
    public static String[] cutStringToBytes(String oneLine){
        String[] cutString = oneLine.split(" ");
        return cutString;
    }
    public static ArrayList<String> cutFileToStrings(){
        ArrayList<String> cutFile = new ArrayList<String>();
        try {
            File kryptoFile = new File("Krypto.txt");
            Scanner kryptoReader = new Scanner(kryptoFile);
            while(kryptoReader.hasNextLine()){
                String currLine = kryptoReader.nextLine();
                if(currLine.length()<=0 || currLine.charAt(currLine.length()-1)==':')
                    continue;
                else
                    cutFile.add(currLine);
            }
        }
        catch(Exception e) {
            System.out.println("Exception accured " + e.getMessage());
        }
        return cutFile;
    }
    public static String whatIsToBeDecyphered(){
        try {
            File kryptoFile = new File("TDecrypt.TXT");
            Scanner kryptoReader = new Scanner(kryptoFile);
            String currLine = kryptoReader.nextLine();
            return currLine;
        }
        catch(Exception e) {

        }
        return "";
    }
    public static void main(String[] args) {
        ArrayList<String> fileCutToStringList = cutFileToStrings();
         whatIs = cutStringToBytes(whatIsToBeDecyphered());
        lettersPos = new PossibleCodes[whatIs.length+1];
        checkEveryCombination(fileCutToStringList );
        for(int i=0;i< lettersPos.length-1;i++){
            if(lettersPos[i]==null)
                System.out.print("?");
            else
            {
                 tryDecoding(i);
            }
        }

    }
}
