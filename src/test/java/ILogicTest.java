import Logic.ILogic;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ILogicTest {

    @Test
    void testIsNumericWithNull() {
        assertFalse(ILogic.isNumeric(null), "Null should return false");
    }

    @Test
    void testIsNumericWithEmptyString() {
        assertFalse(ILogic.isNumeric(""), "Empty string should return false");
    }

    @Test
    void testIsNumericWithSpaces() {
        assertFalse(ILogic.isNumeric("   "), "String with only spaces should return false");
    }

    @Test
    void testIsNumericWithValidInteger() {
        assertTrue(ILogic.isNumeric("4746232"), "Valid integer should return true");
    }

    @Test
    void testIsNumericWithValidNegativeInteger() {
        assertTrue(ILogic.isNumeric("-467536"), "Valid negative integer should return true");
    }

    @Test
    void testIsNumericWithValidPositiveInteger() {
        assertTrue(ILogic.isNumeric("+78576456"), "Valid positive integer should return true");
    }

    @Test
    void testIsNumericWithValidDouble(){
        assertTrue(ILogic.isNumeric("99999999999999999999999999999.999999999999999999999999"), "Valid big double should return true");
    }

    @Test
    void testIsNumericWithValidDecimal() {
        assertTrue(ILogic.isNumeric("587568.87798"), "Valid decimal number should return true");
    }

    @Test
    void testIsNumericWithValidNegativeDecimal() {
        assertTrue(ILogic.isNumeric("-35464.4345879"), "Valid negative decimal number should return true");
    }

    @Test
    void testIsNumericWithValidPositiveDecimal() {
        assertTrue(ILogic.isNumeric("+3468677.989867"), "Valid positive decimal number should return true");
    }

    @Test
    void testIsNumericWithInvalidString() {
        assertFalse(ILogic.isNumeric("abc"), "Non-numeric string should return false");
    }

    @Test
    void testIsNumericWithNumberContainingSpaces() {
        assertFalse(ILogic.isNumeric("34562 354635"), "Number with spaces should return false");
    }

    @Test
    void testIsNumericWithInvalidNumberFormat() {
        assertFalse(ILogic.isNumeric("56786575..567856778"), "Invalid number format should return false");
    }

    @Test
    void testIsNumericWithLeadingAndTrailingSpaces() {
        assertTrue(ILogic.isNumeric("  3456345.34563  "), "Number with leading and trailing spaces should return true after trim");
    }
}
