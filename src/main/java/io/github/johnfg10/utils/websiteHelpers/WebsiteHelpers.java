package io.github.johnfg10.utils.websiteHelpers;

/**
 * Created by johnfg10 on 30/03/2017.
 */
public class WebsiteHelpers {

    /**
     * takes a string and replaces all " " with "+"
     * @param string the string to check
     * @return a string with all spaces converted to "+"
     */
    public static String RemoveSpaces(String string){
        return string.replaceAll(" ", "+");
    }
}
