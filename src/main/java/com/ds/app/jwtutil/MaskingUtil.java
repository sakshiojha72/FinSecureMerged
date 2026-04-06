package com.ds.app.jwtutil;

public class MaskingUtil {
	

    private MaskingUtil() {}
 
    // ── Email masking ──────────────────────────────────────────────────
    // Input:  john.doe@gmail.com
    // Output: joh***@gmail.com
    //
    // Input:  ab@gmail.com
    // Output: ab***@gmail.com  (if name less than 3 chars, show full name part)
    public static String maskEmail(String email) {
        if (email == null || !email.contains("@")) return "***@***.com";
 
        String[] parts = email.split("@");
        String name   = parts[0];   // john.doe
        String domain = parts[1];   // gmail.com
 
        // Show first 3 characters, rest replaced with ***
        String visiblePart = name.length() > 3
                ? name.substring(0, 3)
                : name;
 
        return visiblePart + "***@" + domain;
    }
 
    // ── Phone masking ──────────────────────────────────────────────────
    // Input:  9876543210
    // Output: ******3210
    public static String maskPhone(String phone) {
        if (phone == null || phone.length() < 4) return "******";
 
        String visible = phone.substring(phone.length() - 4);
        return "*".repeat(phone.length() - 4) + visible;
    }
    
 // Generic masker — keeps last N characters visible
    public static String maskLast(int visibleCount, String value) {
        if (value == null) return null;
        if (value.isBlank()) return value;
        if (value.length() <= visibleCount) return value;

        String visible = value.substring(
                value.length() - visibleCount);
        return "X".repeat(value.length() - visibleCount) + visible;
    }
	
	
}
