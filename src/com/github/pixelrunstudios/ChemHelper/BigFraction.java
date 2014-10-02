package com.github.pixelrunstudios.ChemHelper;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Random;

/** <p>
 *  Immutable fractions represented by a ratio of two BigIntegers. Operations use the operations of BigIntegers to modify the private fields.
 *  Most operations of BigFractions return a new BigFraction, which represents the modified value of the fraction.
 *  BigFractions include most basic arithmetic operations, and some special fraction operations including simplifying the fraction.
 *	</p><p>
 *  This class also includes some private static fields to modify the behavior when using the fraction operations, including a MathContext to change the precision when converting to decimals and the mode used to round decimals.
 *  Because of the fact that the value fields are represented by two BigIntegers, theoretically BigFractions can go up to infinite size. However, certain operations are unstable for use after the size overflows the primitive type long.
 *	</p><p>
 *  The constructors of this class are used to create the fractions themselves, along with many methods provided by this class.
 *  There is a special static method to parse fractions from Strings. The fractions must be in the format of x/y. There can be a negative sign in front of the x and/or y, and the values can be larger than the size overflow values of long.
 *  </p>2
 *  @see java.math.BigInteger
 *  @see java.math.BigDecimal
 *  @see java.math.RoundingMode
 *
 *  @author Markus Feng
 */
public class BigFraction extends Number implements Comparable<BigFraction>, Cloneable{

	private static final long serialVersionUID = -1038055196274812800L;

	protected static MathContext mathContext = MathContext.DECIMAL128;
	protected static boolean autoSimplify = false;

	protected BigInteger numerator;
	protected BigInteger denominator;

	/**
	 * Creates a fraction using another fraction.
	 * Copies the numerator and denominator of the fraction and creates a new fraction with the numerator and denominator values.
	 * Same effect as cloning the fraction.
	 * @see #clone()
	 * @param fraction Fraction used for cloning.
	 */
	public BigFraction(BigFraction fraction){
		this(fraction.numerator, fraction.denominator);
	}

	/**
	 * Creates a fraction with the numerator Numerator and the denominator Denominator.
	 * Same as using new BigFraction(BigInteger.valueOf(numerator), BigInteger.valueOf(denominator));
	 * @see #BigFraction(BigInteger, BigInteger)
	 * @param numerator Numerator of the created fraction.
	 * @param denominator Denominator of the created fraction.
	 */
	public BigFraction(long numerator, long denominator) {
		this(BigInteger.valueOf(numerator), BigInteger.valueOf(denominator));
	}

	/**
	 * Creates a fraction with the numerator Numerator and the denominator Denominator.
	 * If the denominator is zero, the constructor throws an ArithmeticException.
	 * @see #BigFraction(long, long)
	 * @param numerator Numerator of the created fraction.
	 * @param denominator Denominator of the created fraction.
	 */
	public BigFraction(BigInteger numerator, BigInteger denominator) {
		if(denominator.equals(BigInteger.valueOf(0))){
			throw new ArithmeticException("BigFraction: zero denominator");
		}
		this.numerator = autoSimplify ? numerator.divide(numerator.gcd(denominator)).abs().multiply(BigInteger.valueOf(numerator.signum() * denominator.signum() >= 0 ? 1 : -1)) : numerator;
		this.denominator = autoSimplify ? denominator.divide(numerator.gcd(denominator)).abs() : denominator;
	}

	/**
	 * Returns the closest double approximation of the fraction.
	 * @return the value of the fraction in the closest double approximation.
	 */
	@Override
	public double doubleValue() {
		return new BigDecimal(numerator).divide(new BigDecimal(denominator), mathContext).doubleValue();
	}

	/**
	 * Returns the closest float approximation of the fraction.
	 * @return the value of the fraction in the closest float approximation.
	 */
	@Override
	public float floatValue() {
		return new BigDecimal(numerator).divide(new BigDecimal(denominator), mathContext).floatValue();
	}

	/**
	 * Returns the closest integer approximation of the fraction.
	 * @return the value of the fraction in the closest integer approximation.
	 */
	@Override
	public int intValue() {
		return new BigDecimal(numerator).divide(new BigDecimal(denominator), mathContext).intValue();
	}

	/**
	 * Returns the closest long approximation of the fraction.
	 * @return the value of the fraction in the closest long approximation.
	 */
	@Override
	public long longValue() {
		return new BigDecimal(numerator).divide(new BigDecimal(denominator), mathContext).longValue();
	}

	/**
	 * Compares to BigFraction bf.
	 * If more than bf, return 1. If less than bf, return -1. If equal to bf, return 0.
	 * Note that this method compares the value, while the next method compares the numerator and the denominator.
	 * @see #equals(Object)
	 * @see #min(BigFraction)
	 * @see #max(BigFraction)
	 * @param bf the BigFraction to compare to.
	 * @return 1 if more than bf, -1 if less than bf, 0 if equal to bf.
	 */
	@Override
	public int compareTo(BigFraction bf) {
		return (denominator.multiply(bf.denominator).compareTo(BigInteger.valueOf(0)) > 0 ? 1 : -1) * numerator.multiply(bf.denominator).compareTo(bf.numerator.multiply(denominator));
	}

	/**
	 * Checks if equal to Object o.
	 * If both the numerator and the denominator are equal, returns true. Otherwise, returns false.
	 * Note that when a compareTo(b) == 0, a equals(b) may be but not necessarily true, but when a compareTo(b) != 0, a equals(b) is always false.
	 * @see #compareTo(BigFraction)
	 * @see #hashCode()
	 * @param o the object to compare to.
	 * @return true if both the numerator and the denominator are equal, false if otherwise.
	 */
	@Override
	public boolean equals(Object o){
		if(!(o instanceof BigFraction)){
			return false;
		}
		BigFraction bf = (BigFraction) o;
		return numerator.equals(bf.numerator) && denominator.equals(bf.denominator);

	}

	/**
	 * Creates a fraction with the same numerator and denominator.
	 * Copies the numerator and denominator of the fraction and creates a new fraction with the numerator and denominator values.
	 * @see #BigFraction(BigFraction)
	 * @return the cloned fraction.
	 */
	@Override
	public Object clone(){
		return new BigFraction(this);
	}

	/**
	 * Returns the hashCode of the object.
	 * Finds the hashCode of the object by finding the sum of the hashCode of numerator and the hashCode of the denominator.
	 * @see #equals(Object)
	 * @return the hashCode of the object.
	 */
	@Override
	public int hashCode(){
		if(!inLongRange()){
			return new Float(numerator.hashCode() / denominator.hashCode()).hashCode();
		};
		Random random = new Random(numerator.longValue());
		long l = denominator.abs().longValue();
		for(long i = 0;i < l; i++){
			random.nextInt();
		}
		return denominator.longValue() > 0?random.nextInt():0 - random.nextInt();
	}

	/**
	 * Returns the String value of the fraction.
	 * Makes the fraction in to a string in the form of x/y where x is the numerator and y is the denominator.
	 * @return the String value of the fraction.
	 */
	@Override
	public String toString(){
		return numerator.toString() + "/" + denominator.toString();
	}

	/**
	 * Converts the BigFraction into the closest BigDecimal approximation.
	 * Accuracy determined by the static fields scale and rounding mode.
	 * @return the BigDecimal converted version of the BigFraction.
	 */
	public BigDecimal toBigDecimal(){
		return new BigDecimal(numerator).divide(new BigDecimal(denominator), mathContext);
	}

	/**
	 * Returns the numerator of the fraction as a long.
	 * @see #getBigIntegerNumerator()
	 * @return the long numerator of the fraction.
	 */
	public long getNumerator() {
		return numerator.longValue();
	}

	/**
	 * Returns the numerator of the fraction as a BigInteger.
	 * @see #getNumerator()
	 * @return the BigInteger numerator of the fraction.
	 */
	public BigInteger getBigIntegerNumerator(){
		return numerator;
	}

	/**
	 * Returns the denominator of the fraction as a long.
	 * @see #getBigIntegerDenominator()
	 * @return the long denominator of the fraction.
	 */
	public long getDenominator() {
		return denominator.longValue();
	}

	/**
	 * Returns the denominator of the fraction as a BigInteger.
	 * @see #getDenominator()
	 * @return the BigInteger denominator of the fraction.
	 */
	public BigInteger getBigIntegerDenominator(){
		return denominator;
	}

	/**
	 * Sets the precision of the MathContext.
	 * This change applies to all BigFractions. By default the precision is 34.
	 * The MathContext determines what to round to and the precision when converting BigFractions in to BigDecimals, floats, and doubles.
	 * @see #setMathContext(MathContext)
	 * @param precision the digits of precision to round to.
	 */
	public static void setPrecision(int precision){
		mathContext = new MathContext(precision, mathContext.getRoundingMode());
	}

	/**
	 * Sets the RoundingMode of the MathContext.
	 * This change applies to all BigFractions. By default the RoundingMode is RoundingMode.HALF_UP.
	 * The MathContext determines what to round to and the precision when converting BigFractions in to BigDecimals, floats, and doubles.
	 * @see #setMathContext(MathContext)
	 * @param r the RoundingMode to use to round integers.
	 */
	public static void setRoundingMode(RoundingMode r){
		mathContext = new MathContext(mathContext.getPrecision(), r);
	}

	/**
	 * Sets the MathContext.
	 * This change applies to all BigFractions. By default the MathContext is MathContext.DECIMAL_128.
	 * The MathContext determines what to round to and the precision when converting BigFractions in to BigDecimals, floats, and doubles.
	 * @param m
	 */
	public static void setMathContext(MathContext m){
		mathContext = m;
	}

	/**
	 * Turns on or off autoSimplify.
	 * This change applies to all BigFractions. By default autoSimplify is false.
	 * If autoSimplify is on, all created fractions will be automatically simplified using the simplifying technique in the method simplify.
	 * @see #simplify()
	 * @param b turn on or off autoSimplify.
	 */
	public static void setAutoSimplify(boolean b){
		autoSimplify = b;
	}

	/**
	 * Simplifies the fraction in to the simplest form.
	 * If the fraction is negative, the numerator becomes negative.
	 * If autoSimplify is turned on, every fraction is automatically simplified.
	 * @see #setAutoSimplify(boolean)
	 * @return the simplified fraction.
	 */
	public BigFraction simplify(){
		return new BigFraction(numerator.divide(numerator.gcd(denominator)).abs().multiply(BigInteger.valueOf(numerator.signum() * denominator.signum() >= 0 ? 1 : -1)), denominator.divide(numerator.gcd(denominator)).abs());
	}

	/**
	 * Returns a BigFraction with the value of (a/c + b/d).
	 * (a is the numerator of the first fraction, b is the numerator of the second fraction, c is the denominator of the frist fraction, and d is the denominator of the second fraction).
	 * The value of the numerator is (ad + cb) and the value of the denominator is (bd).
	 * @param bf the fraction to add.
	 * @return the sum of the fractions.
	 */
	public BigFraction add(BigFraction bf){
		return new BigFraction(numerator.multiply(bf.denominator).add(bf.numerator.multiply(denominator)), denominator.multiply(bf.denominator));
	}

	/**
	 * Returns a BigFraction with the value of (a/c - b/d).
	 * (a is the numerator of the first fraction, b is the numerator of the second fraction, c is the denominator of the frist fraction, and d is the denominator of the second fraction).
	 * The value of the numerator is (ad - cb) and the value of the denominator is (bd).
	 * @param bf the fraction to subtract.
	 * @return the difference of the fractions.
	 */
	public BigFraction subtract(BigFraction bf){
		return new BigFraction(numerator.multiply(bf.denominator).subtract(bf.numerator.multiply(denominator)), denominator.multiply(bf.denominator));
	}

	/**
	 * Returns a BigFraction with the value of (a/c * b/d).
	 * (a is the numerator of the first fraction, b is the numerator of the second fraction, c is the denominator of the frist fraction, and d is the denominator of the second fraction).
	 * The value of the numerator is (ab) and the value of the denominator is (cd).
	 * @param bf the fraction to multiply.
	 * @return the product of the fractions.
	 */
	public BigFraction multiply(BigFraction bf){
		return new BigFraction(numerator.multiply(bf.numerator), denominator.multiply(bf.denominator));
	}

	/**
	 * Returns a BigFraction with the value of (a/c / b/d).
	 * (a is the numerator of the first fraction, b is the numerator of the second fraction, c is the denominator of the frist fraction, and d is the denominator of the second fraction).
	 * The value of the numerator is (ad) and the value of the denominator is (bc).
	 * @param bf the fraction to divide.
	 * @return the quotient of the fractions.
	 */
	public BigFraction divide(BigFraction bf){
		return new BigFraction(numerator.multiply(bf.denominator), denominator.multiply(bf.numerator));
	}

	/**
	 * Returns a BigFraction with the value of Math.pow(this, i).
	 * (a is the numerator of the fraction, c is the denominator if the fraction)
	 * The value of the numerator is (Math.pow(a, i)) and the value of the denominator is (Math.pow(c, i)).
	 * @param i the power to raise to.
	 * @return this fraction to the power of i.
	 */
	public BigFraction pow(int i){
		return new BigFraction(numerator.pow(i), denominator.pow(i));
	}

	/**
	 * Returns a BigFraction with the absolute value of the fraction.
	 * This is the same as new BigFraction(numerator.abs(), denominator.abs()).
	 * @return the absolute value the fraction.
	 */
	public BigFraction abs(){
		return new BigFraction(numerator.abs(), denominator.abs());
	}

	/**
	 * Returns a BigFraction equal to the maximum of this fraction and the argument.
	 * If both arguments are equal, the method returns this fraction.
	 * @see #min(BigFraction)
	 * @see #compareTo(BigFraction)
	 * @param bf the BigFraction to compare to.
	 * @return the maximum of this fraction and bf.
	 */
	public BigFraction max(BigFraction bf){
		return compareTo(bf) < 0 ? new BigFraction(bf) : new BigFraction(this);
	}

	/**
	 * Returns a BigFraction equal to the minimum of this fraction and the argument.
	 * If both arguments are equal, the method returns this fraction.
	 * @see #max(BigFraction)
	 * @see #compareTo(BigFraction)
	 * @param bf the BigFraction to compare to.
	 * @return the minimum of this fraction and bf.
	 */
	public BigFraction min(BigFraction bf){
		return compareTo(bf) > 0 ? new BigFraction(bf) : new BigFraction(this);
	}

	/**
	 * Returns the signum of this fraction.
	 * Returns -1 if this fraction is less than 0, 0 if this fraction is equal to 0, 1 if this fraction is more than 0.
	 * Same as compareTo(new BigFraction(0, 1)).
	 * @see #compareTo(BigFraction)
	 * @return the signum of this fraction.
	 */
	public int signum(){
		return numerator.multiply(denominator).compareTo(BigInteger.valueOf(0));
	}

	/**
	 * Returns true if this fraction is an integer, false if otherwise.
	 * @return true if this fraction is an integer, false if otherwise.
	 */
	public boolean isInteger(){
		return numerator.mod(denominator).equals(BigInteger.valueOf(0));
	}

	/**
	 * Returns true if the BigFraction is in long range.
	 * If both the numerator and the denominator is in long range, the BigFraction is in long range.
	 * @return true if the BigFraction is in long range
	 */
	public boolean inLongRange(){
		return !(numerator.max(BigInteger.valueOf(Long.MAX_VALUE)).equals(numerator) || numerator.min(BigInteger.valueOf(Long.MIN_VALUE)).equals(numerator) || denominator.max(BigInteger.valueOf(Long.MAX_VALUE)).equals(denominator) || denominator.min(BigInteger.valueOf(Long.MIN_VALUE)).equals(denominator));
	}

	/**
	 * Returns true if the BigFraction is in int range.
	 * If both the numerator and the denominator is in int range, the BigFraction is in int range.
	 * @return true if the BigFraction is in int range
	 */
	public boolean inIntRange(){
		return !(numerator.max(BigInteger.valueOf(Integer.MAX_VALUE)).equals(numerator) || numerator.min(BigInteger.valueOf(Integer.MIN_VALUE)).equals(numerator) || denominator.max(BigInteger.valueOf(Integer.MAX_VALUE)).equals(denominator) || denominator.min(BigInteger.valueOf(Integer.MIN_VALUE)).equals(denominator));
	}

	/**
	 * Parses the String s to convert it in to a fraction.
	 * The fractions must be in the format of x/y.
	 * There can be a negative sign in front of the x and/or y, and the values can be larger than the size overflow values of long.
	 * @param s this String to parse.
	 * @return the parsed fraction.
	 */
	public static BigFraction parseFraction(String s){
		try{
			return Parser.parse(s);
		}
		catch (NumberFormatException e){
			throw new NumberFormatException("For input string: \""+ s +"\"");
		}
	}

	private static class Parser{

		private static final char[] ACCEPTABLE_FIRST_DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '-'};
		private static final char[] ACCEPTABLE_DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '/'};
		private static final char[] ACCEPTABLE_AFTER_DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

		private static final byte[] ACCEPTABLE_FIRST_DIGIT_NUMERALS = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, -1};
		private static final byte[] ACCEPTABLE_DIGIT_NUMERALS = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, -2};
		private static final byte[] ACCEPTABLE_AFTER_DIGIT_NUMERALS = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};

		private static BigFraction parse(String s){
			try{
				if(s.equals(null)){
					return null;
				}
				if(s.isEmpty()){
					return null;
				}
				char[] c = s.toCharArray();
				c = removeChar(c, (char) 32);
				c = removeChar(c, (char) 43);
				int l = c.length;
				byte[] b = new byte[l];
				b[0] = findFirst(c[0]);
				int i = 1;
				for(;i < l;i++){
					b[i] = find(c[i]);
					if(b[i] == -2){
						break;
					}
				}
				if(b[i] != -2){
					throw new NumberFormatException();
				}
				i++;
				b[i] = findFirst(c[i]);
				i++;
				for(;i < l; i++){
					b[i] = findAfter(c[i]);
				}
				return fromByteArray(b);
			}
			catch(IllegalArgumentException e){
				throw new NumberFormatException();
			}
			catch(ArrayIndexOutOfBoundsException e){
				throw new NumberFormatException();
			}
		}

		private static byte findFirst(char c){
			int i = 0;
			int j = ACCEPTABLE_FIRST_DIGITS.length;
			for(;i < j; i++){
				if(ACCEPTABLE_FIRST_DIGITS[i] == c){
					return ACCEPTABLE_FIRST_DIGIT_NUMERALS[i];
				}
			}
			throw new IllegalArgumentException();
		}

		private static byte find(char c){
			int i = 0;
			int j = ACCEPTABLE_DIGITS.length;
			for(;i < j; i++){
				if(ACCEPTABLE_DIGITS[i] == c){
					return ACCEPTABLE_DIGIT_NUMERALS[i];
				}
			}
			throw new IllegalArgumentException();
		}

		private static byte findAfter(char c){
			int i = 0;
			int j = ACCEPTABLE_AFTER_DIGITS.length;
			for(;i < j; i++){
				if(ACCEPTABLE_AFTER_DIGITS[i] == c){
					return ACCEPTABLE_AFTER_DIGIT_NUMERALS[i];
				}
			}
			throw new IllegalArgumentException();
		}

		private static BigFraction fromByteArray(byte[] b){
			int numeratorSign = 0;
			int denominatorSign = 0;
			byte[] numeratorArray;
			byte[] denominatorArray;
			BigInteger numerator = BigInteger.valueOf(0);
			BigInteger denominator = BigInteger.valueOf(0);
			int i = 0;
			int j = b.length;
			if(b[0] == -1){
				numeratorSign = -1;
				byte[] c = new byte[j - 1];
				System.arraycopy(b, 1, c, 0, j - 1);
				b = c.clone();
			}
			else{
				numeratorSign = 1;
			}
			j = b.length;
			for(;i < j;i++){
				if(b[i] == -2){
					break;
				}
			}
			if(b[i] != -2){
				throw new IllegalArgumentException();
			}
			numeratorArray = new byte[i];
			System.arraycopy(b, 0, numeratorArray, 0, i);
			i++;
			j = b.length;
			if(b[i] == -1){
				denominatorSign = -1;
				denominatorArray = new byte[j - i - 1];
				System.arraycopy(b, i + 1, denominatorArray, 0, j - i - 1);
			}
			else{
				denominatorSign = 1;
				denominatorArray = new byte[j - i];
				System.arraycopy(b, i, denominatorArray, 0, j - i);
			}
			numerator = byteArrayToBigInteger(numeratorArray, numeratorSign);
			denominator = byteArrayToBigInteger(denominatorArray, denominatorSign);
			return new BigFraction(numerator, denominator);
		}

		private static BigInteger byteArrayToBigInteger(byte[] b, int sign){
			int i = 0;
			BigInteger n = BigInteger.valueOf(0);
			int l = b.length;
			for(;i < l;i++){
				if(b[i] < 0 || b[i] > 9){
					throw new IllegalArgumentException();
				}
				n = n.add(BigInteger.valueOf(10).pow(l-i-1).multiply(BigInteger.valueOf(b[i])));
			}
			if(sign < 0){
				return n.multiply(BigInteger.valueOf(-1));
			}
			else{
				return n;
			}
		}

		private static char[] removeChar(char[] ca, char c){
			int x = 0;
			char[] l = new char[ca.length + 1];
			for(int i = 0; i < ca.length; i++){
				char y = ca[i];
				if(y != c){
					l[x] = y;
					x++;
				}
			}
			char[] n = new char[x];
			System.arraycopy(l, 0, n, 0, x);
			return n;
		}
	}
}
