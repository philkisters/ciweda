package me.kisters.ciweda.util;

import java.util.regex.Pattern;

public class MacAddress {
    private static final Pattern MAC_ADDRESS_PATTERN =
            Pattern.compile("^([0-9A-Fa-f]{2}[:-]){5}([0-9A-Fa-f]{2})$");

    private final String address;

    public MacAddress(String address) {
        if (!isValid(address)) {
            throw new IllegalArgumentException("Invalid MAC address format: " + address);
        }
        this.address = address.toLowerCase();
    }

    public String getAddress() {
        return address;
    }

    public static boolean isValid(String address) {
        return MAC_ADDRESS_PATTERN.matcher(address).matches();
    }

    @Override
    public String toString() {
        return address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MacAddress that = (MacAddress) o;
        return address.equals(that.address);
    }

    @Override
    public int hashCode() {
        return address.hashCode();
    }
}
