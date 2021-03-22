package seedu.fridgefriend.utilities;

import static seedu.fridgefriend.food.FoodCategory.convertStringToFoodCategory;
import static seedu.fridgefriend.food.FoodStorageLocation.convertStringToLocation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.fridgefriend.command.AddCommand;
import seedu.fridgefriend.command.ByeCommand;
import seedu.fridgefriend.command.Command;
import seedu.fridgefriend.command.ExpiringCommand;
import seedu.fridgefriend.command.HelpCommand;
import seedu.fridgefriend.command.ListCommand;
import seedu.fridgefriend.command.RemoveCommand;
import seedu.fridgefriend.command.SearchCommand;
import seedu.fridgefriend.exception.EmptyDescriptionException;
import seedu.fridgefriend.exception.InvalidDateException;
import seedu.fridgefriend.exception.InvalidIndexException;
import seedu.fridgefriend.exception.InvalidInputException;
import seedu.fridgefriend.exception.InvalidQuantityException;
import seedu.fridgefriend.food.Quantity;
import seedu.fridgefriend.food.Weight;

/**
 * Represents an object that deals with making sense of the user command.
 */
public class Parser {

    public static final int COMMAND_WORD = 0;
    public static final int LIMIT = 2;

    /**
     * Returns a Command object based on the user's raw input.
     *
     * @param input user's raw input
     * @return Command object
     * @throws EmptyDescriptionException if the required description field is empty
     * @throws InvalidInputException if the command is not recognised
     * @throws InvalidIndexException if the index given in description is out of bounds
     * @throws InvalidDateException if the date input cannot be parsed
     * @throws InvalidQuantityException if the quantity input cannot be parsed
     */
    public static Command getCommand(String input)
            throws EmptyDescriptionException, InvalidInputException,
            InvalidIndexException, InvalidDateException, InvalidQuantityException {
        String[] parsedInput = parseInput(input);
        Command command = parseCommand(parsedInput);
        return command;
    }

    /**
     * Parses input into command and description.
     *
     * @param input user input string
     * @return String array of command and description
     * @throws InvalidInputException if the input is empty
     */
    public static String[] parseInput(String input) throws InvalidInputException {
        if (input.isEmpty()) {
            throw new InvalidInputException();
        }
        //remove trailing whitespaces and parse input into two separated by a whitespace
        String[] words = input.trim().split("\\s+", Parser.LIMIT);
        if (words.length == Parser.LIMIT) {
            return words;
        } else {
            //return an array of command and empty description
            return new String[]{words[COMMAND_WORD], ""};
        }
    }

    /**
     * Returns the appropriate Command object based on command and description.
     *
     * @param parsedInput string array containing command and description
     * @return Command object
     * @throws EmptyDescriptionException if the required description field is empty
     * @throws InvalidInputException if the command is not recognised
     * @throws InvalidIndexException if the index given in description is out of bounds
     * @throws InvalidDateException if the date input cannot be parsed
     * @throws InvalidQuantityException if the quantity input cannot be parsed
     */
    public static Command parseCommand(String[] parsedInput)
            throws EmptyDescriptionException, InvalidInputException,
            InvalidIndexException, InvalidDateException, InvalidQuantityException {
        String commandString = parsedInput[COMMAND_WORD];
        String description = parsedInput[1];
        Command command;

        switch (commandString.toLowerCase()) {
        case "add":
            command = Parser.getAddCommand(description);
            break;
        case "list":
            command = Parser.getListCommand(description);
            break;
        case "remove":
            command = Parser.getRemoveCommand(description);
            break;
        case "search":
            command = Parser.getSearchCommand(description);
            break;
        case "expiring":
            command = Parser.getExpiringCommand();
            break;
        case "help":
            command = Parser.getHelpCommand();
            break;
        case "bye":
            command = Parser.getByeCommand();
            break;
        default:
            throw new InvalidInputException();
        }
        assert command != null : "Command should not be null";
        return command;
    }

    /**
     * Define arguments format for add food command with quantity.
     * A Pattern object which defines how the input string for food item
     * that should look like. [^/]+ implies 1 or more characters except for '/'
     */
    public static final Pattern FOOD_DATA_ARGS_FORMAT_QUANTITY =
            Pattern.compile("(?<name>[^/]+)"
                    + " /cat (?<category>[^/]+)"
                    + " /exp (?<expiryDate>[^/]+)"
                    + " /loc (?<storageLocation>[^/]+)"
                    + " /qty (?<quantity>[^/]+)");

    /**
     * Define arguments format for add food command with weight.
     * A Pattern object which defines how the input string for food item
     * that should look like. [^/]+ implies 1 or more characters except for '/'
     */
    public static final Pattern FOOD_DATA_ARGS_FORMAT_WEIGHT =
            Pattern.compile("(?<name>[^/]+)"
                    + " /cat (?<category>[^/]+)"
                    + " /exp (?<expiryDate>[^/]+)"
                    + " /loc (?<storageLocation>[^/]+)"
                    + " /wgt (?<weight>[^/]+)");

    /**
     * Parses description into name, foodCategory, expiryDate and storageLocation.
     * Matcher objects will try to parse a string according to the Pattern we define
     * like above FOOD_DATA_ARGS_FORMAT. For other future parsers can copy the usage here.
     *
     * @param foodDescription the string in the required format of food description
     * @return a new AddCommand for Food
     * @throws EmptyDescriptionException if the description is empty
     * @throws InvalidInputException if the description cannot parse
     * @throws InvalidDateException if the date input cannot be parsed
     * @throws InvalidQuantityException if the quantity input cannot be parsed
     */
    public static Command parseFoodDescription(String foodDescription)
            throws EmptyDescriptionException, InvalidInputException, InvalidDateException, InvalidQuantityException {
        if (foodDescription.isEmpty()) {
            throw new EmptyDescriptionException();
        }
        Matcher matcherQuantity = FOOD_DATA_ARGS_FORMAT_QUANTITY.matcher(foodDescription.trim());
        Matcher matcherWeight = FOOD_DATA_ARGS_FORMAT_WEIGHT.matcher(foodDescription.trim());

        // Validate foodDescription string format
        if (matcherQuantity.matches()) {
            Quantity quantity = new Quantity(matcherQuantity.group("quantity"));
            return new AddCommand(matcherQuantity.group("name"),
                    convertStringToFoodCategory(matcherQuantity.group("category")),
                    matcherQuantity.group("expiryDate"),
                    convertStringToLocation(matcherQuantity.group("storageLocation")),
                    quantity);
        } else if (matcherWeight.matches()) {
            Weight weight = new Weight(matcherWeight.group("weight"));
            return new AddCommand(matcherWeight.group("name"),
                    convertStringToFoodCategory(matcherWeight.group("category")),
                    matcherWeight.group("expiryDate"),
                    convertStringToLocation(matcherWeight.group("storageLocation")),
                    weight);
        } else {
            throw new InvalidInputException();
        }
    }

    /**
     * Returns an AddCommand object based on description.
     *
     * @param description description for command
     * @return AddCommand object
     * @throws EmptyDescriptionException if the description is empty
     * @throws InvalidInputException if the description cannot parse
     * @throws InvalidDateException if the date input cannot be parsed
     * @throws InvalidQuantityException if the quantity input cannot be parsed
     */
    public static Command getAddCommand(String description)
            throws EmptyDescriptionException, InvalidInputException, InvalidDateException, InvalidQuantityException {
        Command addCommand = parseFoodDescription(description);
        return addCommand;
    }

    /**
     * Returns a ListCommand object based on description.
     *
     * @param description description for command
     * @return ListCommand object
     */
    public static Command getListCommand(String description) {
        Command listCommand = new ListCommand(description);
        return listCommand;
    }

    /**
     * Returns a RemoveCommand object based on description.
     *
     * @param description description for command
     * @return RemoveCommand object
     * @throws EmptyDescriptionException if the description is empty
     * @throws InvalidIndexException if the index given in description is out of bounds
     */
    public static Command getRemoveCommand(String description)
            throws EmptyDescriptionException, InvalidIndexException {
        int index = parseIntegerDescription(description);
        Command removeCommand = new RemoveCommand(index);
        return removeCommand;
    }

    /**
     * Returns a SearchCommand object based on description.
     *
     * @param description description for command
     * @return SearchCommand object
     * @throws EmptyDescriptionException if the description is empty
     */
    public static Command getSearchCommand(String description) throws EmptyDescriptionException {
        Command searchCommand = new SearchCommand(description);
        return searchCommand;
    }

    /**
     * Returns an ExpiringCommand object.
     */
    private static Command getExpiringCommand() {
        Command expiringCommand = new ExpiringCommand();
        return expiringCommand;
    }

    /**
     * Returns a HelpCommand object.
     *
     * @return a list of instructions on how to use the various commands
     */
    public static Command getHelpCommand() {
        Command helpCommand = new HelpCommand();
        return helpCommand;
    }

    /**
     * Returns a ByeCommand object.
     *
     * @return a ByeCommand to exit the program
     */
    public static Command getByeCommand() {
        Command byeCommand = new ByeCommand();
        return byeCommand;
    }

    /**
     * Parses the description into an integer.
     *
     * @param description description for command
     * @return integer index
     * @throws EmptyDescriptionException if the description is empty
     * @throws InvalidIndexException if the description is not a number
     */
    public static int parseIntegerDescription(String description)
            throws EmptyDescriptionException, InvalidIndexException {
        if (description.isEmpty()) {
            throw new EmptyDescriptionException();
        }

        try {
            int index = Integer.parseInt(description);
            return index;
        } catch (Exception e) {
            throw new InvalidIndexException(e);
        }
    }

}
