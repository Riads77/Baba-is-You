package fr.umlv.baba;


/**
 * Word is an element. Unlike Item, they have always the PUSH property. They are not depending of rules. They create it.
 * They are three type of words : Name, Operator and Property. But unlike Operator, the other one are considered like Rule.
 * @see Element
 * @see Name
 * @see Operator
 * @see Property
 * @see Rule
 */
public interface Word extends Element {}
