package beginnerLevel.problem02IBANValidator;

public class IBANValidator {

    private static final int IBAN_LENGTH = 26;
    private static final String COUNTRY_CODE = "TR";

    public static boolean isValidIBAN(String iban) {
        if (iban == null || iban.isEmpty()) {
            return false;
        }

        if (iban.length() != IBAN_LENGTH) {
            return false;
        }

        if (!iban.startsWith(COUNTRY_CODE)) {
            return false;
        }

        String numericPart = iban.substring(2);
        return numericPart.matches("\\d{24}");
    }

    public static void main(String[] args) {
        testIBAN("TR330006100519786457841326", true);
        testIBAN("TR33000610051978645784132", false);
        testIBAN("US330006100519786457841326", false);
        testIBAN("TR33000610051978645784132A", false);
        testIBAN("TR 330006100519786457841326", false);
        testIBAN(null, false);
    }

    private static void testIBAN(String iban, boolean expected) {
        boolean result = isValidIBAN(iban);
        String status = result == expected ? "PASS" : "FAIL";
        System.out.println(status + " - " + iban + " -> " + result);
    }
}
