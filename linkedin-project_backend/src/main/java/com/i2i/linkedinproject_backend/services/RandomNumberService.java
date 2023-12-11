package com.i2i.linkedinproject_backend.services;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.i2i.linkedinproject_backend.exceptionHandling.exceptions.ExternalAPIException;

/*
 * Description: This service class exposes various methods for calling a Random Number Generator API as well as for parsing the results and using the results to generate
 * a secret code in accordance to the passed in game settings parameters.
 * 
 * @author Arhian Albis Ramos
 */
@Service
public class RandomNumberService {	
	private static final int SECRET_QUANTITY = 10; 		// quantity of secret values that are returned from each API call
	
	/*
	 * Description: this method generates the appropriate external API URL in accordance to the secretLength @see GameSettings and the secretMax @see GameSettings.
	 * Each API call using this method ot generate the URL returns enough random integers to create 10 secret codes, regardless of the length. The reasoning 
	 * behind this is that if we would like a secret code without any repeated digits we would have enough secret codes in the response so that at least one of those does 
	 * not contain repeated digits so we would not have to make another API call.
	 * 
	 * @param secretLength @see GameSettings
	 * @param digitMax @see GameSettings
	 * 
	 * @return url: the correctly formatted URL in accordance to the passed in parameters.
	 */
	public static String getURL(int secretLength, int digitMax) {
		return "https://www.random.org/integers/?num="+secretLength*SECRET_QUANTITY+"&min=0&max="+digitMax+"&col=1&base=10&format=plain&rnd=new";
	}
	
	/*
	 * Description: This method makes GET request to the passed in URL and returns the response in a String form. @see RestTemplate @see RestTemplate#getForObject()
	 * 
	 *  @param URL the URL of the API endpoint which you want to call
	 *  @return The response body in string form.
	 */
	public static String getNumbers(String URL) {
		RestTemplate restTemplate = new RestTemplate();
		return restTemplate.getForObject(URL, String.class);
	}
	
	
	/*
	 * Description: This method parses the response from the API call and returns a string of continous integers in a string. It assumes the passed in string is a string 
	 * of digits each separated by a line feed character.
	 * 
	 * @param unparsed string. It assumes its a string of characters (in this case they are integers wrapped as a string) each separated by a line feed character.
	 * @return a continous string of characters
	 */
	public static String parseResponse(String unparsed){
		String[] parsedNumbersArray = unparsed.split("\n");
		StringBuilder parsedNumbersString= new StringBuilder();
		
		for(String digit : parsedNumbersArray) {
			parsedNumbersString.append(digit);
		}
		
		return parsedNumbersString.toString();
	}
	
	
	/*
	 * Description: This method takes a string of integers and checks if there are repeated digits in that string or if each of the digits are unique. Returns true if 
	 * at least one of the integers is repeated or false otherwise.
	 * 
	 * @param secret: a string of integers. Assumes this string contains only integers
	 * @return true if the passed in string has repeated digits or false otherwise.
	 */
	public static boolean hasRepeats(String secret) {
		int[] frequency = new int[10]; // the indices in this array represent the number of times the number corresponding to that index has shown up in secret 

		// iterates throught secret
		for(int i=0; i<secret.length(); i++) {
			// turns the current character into an int type
			int digit = Character.getNumericValue(secret.charAt(i));
			// registers the fact that the current number has shown up in the frequency array
			frequency[digit]++;
			// if a number has shown up more than once, then it is repeated
			if(frequency[digit] > 1) return true;
		}
		// else secret has no repeats
		return false;
	}
	
	/*
	 * Description: This method takes different GameSettings parameters @see GameSettings, it generates the appropiate external random number API URL @ getURL()
	 * it calls the external Random Number Generator API @see getNumbers(), it parses the response @see parseResponse() and it generates a secret in accordance 
	 * to isRepeatedDigits @see GameSettings
	 * 
	 * @param isRepeatedDigits: whether the secret is allowed to have repeats @see GameSettings
	 * @param secretLength: number of digits allowed in the secret @see GameSettings
	 * @param digitMax: high end of the range for each digit in secret @see GameSettings
	 * 
	 * @return secret: randomly generated secret value that follows the set of parameters passed in. @see GameSettings
	 */
	public String generateSecret(boolean isRepeatedDigits, int secretLength, int digitMax) {
		int begIndex=0, endIndex=secretLength, secretCounter=0;
		
		try {
			String apiResponse = getNumbers(getURL(secretLength, digitMax));
			
			String parsedNumbers = parseResponse(apiResponse);
			
			// gets the first secret value from our parsedNumbers string
			String secret = parsedNumbers.substring(begIndex, endIndex);
			// increments the secret counter (the number of secret values from parseNumbers that we have used up)
			secretCounter++;
			
			// if repeated digits are allowed, return that secret
			if(isRepeatedDigits) return secret;
		
			// else (if repeatedDigits are not allowed) while secret has repeats grab a new secret from parsedNumbers 
			while(hasRepeats(secret)) {
				// if we run out of secrets (if all 10 secrets in parsedNumbers have repeats), recurse (calls itself which makes another API call, getting a new set of random numbers)
				if(secretCounter == SECRET_QUANTITY) generateSecret(isRepeatedDigits, secretLength, digitMax);
				
				// grabs the next secret from parsedNumbers and increments secretCounter
				begIndex = endIndex;
				endIndex+=secretLength;
				secret = parsedNumbers.substring(begIndex, endIndex);
				secretCounter++;
			}
			
			return secret;
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new ExternalAPIException("Random number API error, please try again.");
		}
	}
}