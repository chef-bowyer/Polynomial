package poly;

import java.io.*;
import java.util.StringTokenizer;

/**
 * This class implements a term of a polynomial.
 * 
 * @author runb-cs112
 *
 */
class Term {
	/**
	 * Coefficient of term.
	 */
	public float coeff;
	
	/**
	 * Degree of term.
	 */
	public int degree;
	
	/**
	 * Initializes an instance with given coefficient and degree.
	 * 
	 * @param coeff Coefficient
	 * @param degree Degree
	 */
	public Term(float coeff, int degree) {
		this.coeff = coeff;
		this.degree = degree;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	
	public boolean equals(Object other) {
		return other != null &&
		other instanceof Term &&
		coeff == ((Term)other).coeff &&
		degree == ((Term)other).degree;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		if (degree == 0) {
			return coeff + "";
		} else if (degree == 1) {
			return coeff + "x";
		} else {
			return coeff + "x^" + degree;
		}
	}
}

/**
 * This class implements a linked list node that contains a Term instance.
 * 
 * @author runb-cs112
 *
 */
class Node {
	
	/**
	 * Term instance. 
	 */
	Term term;
	
	/**
	 * Next node in linked list. 
	 */
	Node next;
	
	/**
	 * Initializes this node with a term with given coefficient and degree,
	 * pointing to the given next node.
	 * 
	 * @param coeff Coefficient of term
	 * @param degree Degree of term
	 * @param next Next node
	 */
	public Node(float coeff, int degree, Node next) {
		term = new Term(coeff, degree);
		this.next = next;
	}
}

/**
 * This class implements a polynomial.
 * 
 * @author runb-cs112
 *
 */
public class Polynomial {
	
	/**
	 * Pointer to the front of the linked list that stores the polynomial. 
	 */ 
	Node poly;
	
	/** 
	 * Initializes this polynomial to empty, i.e. there are no terms.
	 *
	 */
	public Polynomial() {
		poly = null;
	}
	
	/**
	 * Reads a polynomial from an input stream (file or keyboard). The storage format
	 * of the polynomial is:
	 * <pre>
	 *     <coeff> <degree>
	 *     <coeff> <degree>
	 *     ...
	 *     <coeff> <degree>
	 * </pre>
	 * with the guarantee that degrees will be in descending order. For example:
	 * <pre>
	 *      4 5
	 *     -2 3
	 *      2 1
	 *      3 0
	 * </pre>
	 * which represents the polynomial:
	 * <pre>
	 *      4*x^5 - 2*x^3 + 2*x + 3 
	 * </pre>
	 * 
	 * @param br BufferedReader from which a polynomial is to be read
	 * @throws IOException If there is any input error in reading the polynomial
	 */
	public Polynomial(BufferedReader br) throws IOException {
		String line;
		StringTokenizer tokenizer;
		float coeff;
		int degree;
		
		poly = null;
		
		while ((line = br.readLine()) != null) {
			tokenizer = new StringTokenizer(line);
			coeff = Float.parseFloat(tokenizer.nextToken());
			degree = Integer.parseInt(tokenizer.nextToken());
			poly = new Node(coeff, degree, poly);
		}
	}
	
	
	/**
	 * Returns the polynomial obtained by adding the given polynomial p
	 * to this polynomial - DOES NOT change this polynomial
	 * 
	 * @param p Polynomial to be added
	 * @return A new polynomial which is the sum of this polynomial and p.
	 */
	public Polynomial add(Polynomial p) {
		
		Node poly1 = this.poly, poly2 = p.poly, ret = new Node(0,0,null), front = ret;
		
		while(poly1!=null&&poly2!=null){
			if(poly1.term.degree==poly2.term.degree){ //Adds the Polynomials in List1 & 2 that have matching degrees
				ret.next=new Node(poly1.term.coeff+poly2.term.coeff,poly1.term.degree,null);
				poly1=poly1.next;
				poly2=poly2.next;
				if(ret.next.term.coeff==0)ret.next=null;
				else ret=ret.next;
			}
			else if(poly1.term.degree<poly2.term.degree){ //Adds the Polynomials in List 1 that dont have a matching degree in List 2
				ret.next=new Node(poly1.term.coeff,poly1.term.degree,null);
				poly1=poly1.next;
				ret=ret.next;
			}
			else if(poly2.term.degree<poly1.term.degree){ //Adds the Polynomials in List 2 that dont have a matching degree in List 1
				ret.next=new Node(poly2.term.coeff,poly2.term.degree,null);
				poly2=poly2.next;
				ret=ret.next;
			}
		}
		
		while(poly1!=null&&poly2==null){ //Adds all of the polynomials in List 1 that are of a higher degree than List 2
			ret.next=new Node(poly1.term.coeff,poly1.term.degree,null);
			poly1=poly1.next;
			ret=ret.next;
		}
		while(poly1==null&&poly2!=null){ //Adds all of the polynomials in List 2 that are of a higher degree than List 1
			ret.next=new Node(poly2.term.coeff,poly2.term.degree,null);
			poly2=poly2.next;
			ret=ret.next;
		}
		
		p.poly=front.next; //the first node of front is null
		return p;
	}
	
	/**
	 * Returns the polynomial obtained by multiplying the given polynomial p
	 * with this polynomial - DOES NOT change this polynomial
	 * 
	 * @param p Polynomial with which this polynomial is to be multiplied
	 * @return A new polynomial which is the product of this polynomial and p.
	 * @throws IOException 
	 */
	public Polynomial multiply(Polynomial p) throws IOException {
		Polynomial ret = new Polynomial();
			for(Node poly1=p.poly;poly1!=null;poly1=poly1.next){ //simple O(n^2) algorithm
				String temp = "";
				for(Node poly2=poly;poly2!=null;poly2=poly2.next) temp = (poly1.term.coeff*poly2.term.coeff)+" "+(poly1.term.degree+poly2.term.degree)+"\n" + temp;
				ret=ret.add(new Polynomial(new BufferedReader(new StringReader(temp))));//makes a string for each monomial that Polynomial fcan use to construct a new polynomial
			}
		return ret;
	}
	
	/**
	 * Evaluates this polynomial at the given value of x
	 * 
	 * @param x Value at which this polynomial is to be evaluated
	 * @return Value of this polynomial at x
	 */
	public float evaluate(float x){ 
		float sum = 0;
		for (Node current = poly ; current != null ; current = current.next) {
			sum+= current.term.coeff*Math.pow(x, current.term.degree);
		}
		return sum;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		String retval;
		
		if (poly == null) {
			return "0";
		} else {
			retval = poly.term.toString();
			for (Node current = poly.next ;
			current != null ;
			current = current.next) {
				retval = current.term.toString() + " + " + retval;
			}
			return retval;
		}
	}
}
