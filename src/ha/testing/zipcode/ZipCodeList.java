package ha.testing.zipcode;

import java.io.Serializable;

public class ZipCodeList implements Serializable
{
    private static final long serialVersionUID = -4670915090781162038L;
    String city;
    String ZipCode;
    ZipCodeList next;
    
    public ZipCodeList(String c, String z, ZipCodeList n)
    {
        city = c;
        ZipCode = z;
        next = n;
    }
}