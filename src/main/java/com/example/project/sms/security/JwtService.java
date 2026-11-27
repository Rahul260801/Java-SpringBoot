package com.example.project.sms.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;


//update-3
@Service
public class JwtService {
    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expiration}")
    private  long jwtExpiration;
    //we need to convert oursecret string to usauble secret key for jwt signing
    private SecretKey getSignKey(){
        byte[] keyBuytes = Decoders.BASE64.decode(secret);//our secret is 0base 64 string converts to bytes
        return Keys.hmacShaKeyFor(keyBuytes);// it convertes bytes to secret key it later jwt uses for signing
    }

    public String generateToken(UserDetails userDetails){
        return Jwts.builder().subject(userDetails.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis()+jwtExpiration))
                .signWith(getSignKey())
                .compact();
    }
    public String extractUsername(String token){// take input as jwt token and in token return stored username but not directly
        return extractClaim(token, Claims::getSubject); //from jwtclaim we need subject so we stored username in jwt subjectclaim
    }
    public <T> T extractClaim(String token, Function<Claims,T> claimsResolver){
        Claims claims = extractAllClaims(token);//we can extract different info from jwt other than username like subject,expiration ,issuedat
        return claimsResolver.apply(claims);//extractclaim get actual claim from extractallclaim basically it select required info
    }
    private Claims extractAllClaims(String token){// open and verifies the token
        return Jwts.parser().verifyWith(getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
    public boolean isTokenValid(String token,UserDetails userDetails){//token is jwt sent by client and user details are details of user curently identified
        String username=extractUsername(token);
        return username.equals(userDetails.getUsername())&& !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token,Claims::getExpiration);
    }
}
