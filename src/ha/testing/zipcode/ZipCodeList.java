package ha.testing.zipcode;

import java.io.Serializable;

public interface ZipCodeList extends Serializable
{
    public String getCity();
    public String getZipCode();
    public ZipCodeList getNext();
}
