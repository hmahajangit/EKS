package com.siemens.nextwork.tag.util;

import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.JWTParser;
import com.siemens.nextwork.tag.constants.NextworkConstants;
import com.siemens.nextwork.tag.exception.RestBadRequestException;
import org.springframework.stereotype.Component;

import java.text.ParseException;


@Component
public class CommonUtils {

    private CommonUtils() {
        super();
    }

    public static String getEmailId(String authorizationHeader) throws Exception {
        try {
            String authToken = authorizationHeader.substring(7);
            JWT jwt = JWTParser.parse(authToken);
            JWTClaimsSet jwtClaimSet = jwt.getJWTClaimsSet();
            return jwtClaimSet.getClaims().get(NextworkConstants.EMAIL).toString();
        }
        catch (ParseException e) {
            throw new ParseException("ParseException while extracting email from token: " + e.getMessage(), 0);
        }
        catch (RestBadRequestException e){
            throw new RestBadRequestException("Exception while extracting email from token: " + e.getMessage());
        }
        catch (Exception e) {
            throw new Exception("Exception while extracting email from token: " + e.getMessage());
        }
    }
}
