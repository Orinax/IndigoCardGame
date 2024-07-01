package indigo

const val DIAMONDS = "\u2666"
const val HEARTS = "\u2665"
const val SPADES = "\u2660"
const val CLUBS = "\u2663"
const val ONE_HAND = 6
const val STARTING_TABLE_CARDS = 4
const val POINTS_FOR_MOST_CARDS = 3

val cardRanks = listOf<Any>('A', '2', '3', '4', '5', '6', '7', '8', '9', "10", 'J', 'Q', 'K')
val cardSuits = listOf(DIAMONDS, HEARTS, SPADES, CLUBS)

/** A class that resembles a deck of cards. */
class CardDeck(cardRanks: List<Any>, cardSuits: List<String>) {
    var cardsInDeck = mutableListOf<String>()
    init {
        for (suit in cardSuits) {
            for (rank in cardRanks) {
                // print("$rank$suit ")
                cardsInDeck.add("$rank$suit")
            }
        }
    }

    /** Reset the order of the cards to be organized by suit from A to K. */
    fun reset() {
        cardsInDeck.clear()
        for (suit in cardSuits) {
            for (rank in cardRanks) {
                cardsInDeck.add("$rank$suit")
            }
        }
        println("Card deck is reset.")
    }

    /** Shuffle the cards to mix up the order of the cards. */
    fun shuffleDeck() {
        for ((deckIndex, card) in cardsInDeck.shuffled().withIndex()) {
            cardsInDeck[deckIndex] = card
        }
        // println("Card deck is shuffled.")
    }

    /** Remove a number of cards from the deck and return a list of all cards that have been removed from the deck. */
    fun removeCardsFromDeck(cardsToRemove: Int): List<String> {
        val takenCards = cardsInDeck.take(cardsToRemove)
        cardsInDeck.removeAll(cardsInDeck.take(cardsToRemove))
        return takenCards
    }

    /** Display all card ranks, Ace through King. */
    fun displayRanks() {
        // Print all ranks
        for (rank in cardRanks) {
            print("$rank ")
        }
        println()
    }

    /** Display all card suits: Diamonds, Hearts, Spades, Clubs. */
    fun displaySuits() {
        // Print all suits.
        for (suit in cardSuits) {
            print("$suit ")
        }
        println()
    }

    /** Shuffle all cards in the deck and then display them. */
    fun displayShuffledDeck() {
        for (suit in cardSuits.shuffled()) {
            for (rank in cardRanks.shuffled()) {
                print("$rank$suit ")
            }
        }
    }
}

/** A class used to manage the game for players. Equivalent to a dealer in a real life card game. */
class GameHandler() {
    var keepPlaying = false
    val cardWinners = mutableListOf<String>()
    private var identifiedFirstPlayer = false

    /** Print the title of the game. */
    fun printGameTitle() {
        println("Indigo Card Game")
    }

    /** Set either playerOne or playerTwo as the first player for the game. */
    fun decideFirstPlayer(playerOne: Player, playerTwo: Player) {
        while (!identifiedFirstPlayer) {
            println("Play first?")
            val playerAnswer = readln()
            if (playerAnswer.lowercase() == "yes") {
                playerOne.isFirstPlayer = true
                playerTwo.isFirstPlayer = false
                identifiedFirstPlayer = true
            } else if (playerAnswer.lowercase() == "no") {
                playerOne.isFirstPlayer = false
                playerTwo.isFirstPlayer = true
                identifiedFirstPlayer = true
            }
        }
    }

    /** Display a list of actions that the user can choose from:
     * Actions are: reset, shuffle, get, exit, look
     */
    fun displayActionMenu(deck: CardDeck, table: Table, playerOne: Player, playerTwo: Player) {
        println("Choose an action (reset, shuffle, get, exit):")
        val playerChoice = readln()
        if (playerChoice == "reset") {
            deck.reset()
        } else if (playerChoice == "shuffle") {
            deck.shuffleDeck()
        } else if (playerChoice == "get") {
            deck.removeCardsFromDeck(0)
        } else if (playerChoice == "exit") {
            keepPlaying = exitGame()
        } else if (playerChoice == "look") {
            lookAtCards(deck, table, playerOne, playerTwo)
        } else {
            println("Wrong action.")
        }
    }

    /** Deal cards from the deck to the table and both players. */
    fun dealCards(deck: CardDeck, table: Table, playerOne: Player, playerTwo: Player) {
        table.cardsOnTable.addAll(deck.removeCardsFromDeck(STARTING_TABLE_CARDS))
        playerOne.cardsInHand.addAll(deck.removeCardsFromDeck(ONE_HAND))
        playerTwo.cardsInHand.addAll(deck.removeCardsFromDeck(ONE_HAND))
    }

    /** Deal cards from the deck to both players. */
    fun dealCards(deck: CardDeck, playerOne: Player, playerTwo: Player) {
        playerOne.cardsInHand.addAll(deck.removeCardsFromDeck(ONE_HAND))
        playerTwo.cardsInHand.addAll(deck.removeCardsFromDeck(ONE_HAND))
    }

    /** Display all cards in the game and where they currently are. */
    private fun lookAtCards(deck: CardDeck, table: Table, playerOne: Player, playerTwo: Player) {
        print("Cards remaining in the deck: ")
        displayCards(deck.cardsInDeck)
        print("Cards on the table: ")
        displayCards(table.cardsOnTable)
        print("Cards in Player One's hand: ")
        displayCards(playerOne.cardsInHand)
        print("Cards in Player Two's hand: ")
        displayCards(playerTwo.cardsInHand)
    }

    /** Display all cards for any set of cards. */
    private fun displayCards(cards: MutableList<String>) {
        for (card in cards) {
            print("$card ")
        }
        println()
    }

    /** End the game and exit the program. */
    fun exitGame(): Boolean {
        println("Game Over")
        return true
    }

    /** Announce and display the initial set of cards on the table. */
    fun beginPlay(table: Table) {
        print("Initial cards on the table: ")
        displayCards(table.cardsOnTable)
        println()
    }

    /** Announce the number of cards on the table, and display the top card on the table. */
    fun announceCardsOnTable(table: Table) {
        if (table.cardsOnTable.size == 0) {
            println("No cards on the table")
        } else {
            print("${table.cardsOnTable.size} cards on the table, and the top card is ${table.cardsOnTable.last()}")
            println()
        }
    }

    /** Announce the current score and number of cards won for each player. */
    fun announceCurrentScore(playerOne: Player, playerTwo: Player) {
        println("Score: Player ${playerOne.score} - Computer ${playerTwo.score}")
        println("Cards: Player ${playerOne.cardsWon} - Computer ${playerTwo.cardsWon}")
    }

    /** Check the played card to see if matches either rank or suit of the top table card. */
    private fun checkCardMatch(table: Table): Boolean {
        val isMatch: Boolean
        if (table.cardsOnTable.size > 1) {
            val cardOnTopRank = table.cardsOnTable.elementAt(table.cardsOnTable.size - 2)[0]
            var cardOnTopSuit = table.cardsOnTable.elementAt(table.cardsOnTable.size - 2)[1]
            val cardJustPlayedRank = table.cardsOnTable.elementAt(table.cardsOnTable.size - 1)[0]
            var cardJustPlayedSuit = table.cardsOnTable.elementAt(table.cardsOnTable.size - 1)[1]
            if (cardOnTopSuit == '0') {  // The suit for cards of rank 10 will be at index 2, not 1.
                cardOnTopSuit = table.cardsOnTable.elementAt(table.cardsOnTable.size - 2)[2]
            }
            if (cardJustPlayedSuit == '0') {  // The suit for cards of rank 10 will be at index 2, not 1.
                cardJustPlayedSuit = table.cardsOnTable.elementAt(table.cardsOnTable.size - 1)[2]
            }
            if ((cardOnTopSuit == cardJustPlayedSuit) || (cardOnTopRank == cardJustPlayedRank)) {
                isMatch = true
                return isMatch
            } else {
                isMatch = false
                return isMatch
            }
        }
        return false
    }

    /** Check the cards on the table to see how many points there are in the set of cards. */
    fun checkCardPoints(table: Table): Int {
        var pointsEarned = 0
        for (card in table.cardsOnTable) {
            if (card.length == 3) {
                pointsEarned++  // A card of length 3 is a 10 of any suit. This will always be 1 point.
            } else {
                if (card[0] == 'A' || card[0] == 'J' || card[0] == 'Q' || card[0] == 'K') {
                    pointsEarned++
                }
            }
        }
        return pointsEarned
    }

    /** Handle playerOne's turn. */
    private fun handlePlayerTurn(playerName: String, gameHandler: GameHandler, playerOne: Player, playerTwo: Player,
                         table: Table) {
        val cardMatch = checkCardMatch(table)
        if (cardMatch) {
            checkCardPoints(table)
            if (playerName == "Player") {
                playerOne.earnPoints(gameHandler, table)
                cardWinners.add(playerOne.winCards(table))
            } else {
                playerTwo.earnPoints(gameHandler, table)
                cardWinners.add(playerTwo.winCards(table))
            }
            println("${cardWinners.last()} wins cards")
            announceCurrentScore(playerOne, playerTwo)
        }
    }

    /** Turn pattern to use when playerOne is the first player. */
    fun playerOneFirst(gameHandler: GameHandler, playerOne: Player, playerTwo: Player, table: Table) {
        gameHandler.announceCardsOnTable(table)
        playerOne.checkCardsInHand()
        val exitNow = playerOne.playACard(table, gameHandler)
        println()
        if (!exitNow) {  // If false, gameplay will continue. If true, gameplay will end now.
            handlePlayerTurn("Player", gameHandler, playerOne, playerTwo, table)
            gameHandler.announceCardsOnTable(table)
            playerTwo.playACard(table, gameHandler)
            handlePlayerTurn("Computer", gameHandler, playerOne, playerTwo, table)
        }
    }

    /** Turn pattern to use when playerTwo is the first player. */
    fun playerTwoFirst(gameHandler: GameHandler, playerOne: Player, playerTwo: Player, table: Table) {
        gameHandler.announceCardsOnTable(table)
        playerTwo.playACard(table, gameHandler)
        handlePlayerTurn("Computer", gameHandler, playerOne, playerTwo, table)
        gameHandler.announceCardsOnTable(table)
        playerOne.checkCardsInHand()
        playerOne.playACard(table, gameHandler)
        handlePlayerTurn("Player", gameHandler, playerOne, playerTwo, table)
        println()
    }
}

/** A class used to manage player cards and actions. */
class Player(playerType: Boolean) {
    var cardsInHand = mutableListOf<String>()
    var isFirstPlayer = true
    var isHumanPlayer: Boolean
    var score = 0
    var cardsWon = 0
    val name: String

    init {
        if (playerType) {
            isHumanPlayer = true
            name = "Player"
        } else {
            isHumanPlayer = false
            name = "Computer"
        }
    }

    /** Display a numbered list of the current set of cards in a player's hand. */
    fun checkCardsInHand() {
        var cardNumber = 0
        print("Cards in hand: ")
        for (card in cardsInHand) {
            print("${++cardNumber})$card ")
        }
        println()
    }

    /** Remove a card from the player's hand and add it to the set of cards on the table.
     * This function returns an exitNow Boolean flag. Returning true will cause the program to end.
     * Returning false will allow the program to continue running.
     * */
    fun playACard(table: Table, gameHandler: GameHandler): Boolean {
        var cardPlayed = false
        var exitNow = false

        if (isHumanPlayer) {
            while (!cardPlayed) {
                println("Choose a card to play (1-${cardsInHand.size}):")
                val playerInput = readln()
                if (playerInput == "exit") {
                    gameHandler.keepPlaying = gameHandler.exitGame()
                    exitNow = true
                    return exitNow
                } else {
                    val cardChoice = playerInput.toIntOrNull() ?: -1
                    if ((cardChoice > 0) && (cardChoice <= cardsInHand.size)) {
                        table.cardsOnTable.add(cardsInHand.elementAt(cardChoice - 1))
                        cardsInHand.removeAt(cardChoice - 1)
                        cardPlayed = true
                        return exitNow
                    }
                }
            }
        } else {
            table.cardsOnTable.add(cardsInHand.elementAt(0))
            cardsInHand.removeAt(0)
            println("Computer plays ${table.cardsOnTable.last()}")
        }
        println()
        return exitNow
    }

    /** Add points to a player's score */
    fun earnPoints(gameHandler: GameHandler, table: Table) {
        score += gameHandler.checkCardPoints(table)
    }

    /** Add cards to a player's count of cards won.
     * Return: name of the player who won the cards.
     */
    fun winCards(table: Table): String {
        for (card in table.cardsOnTable) {
            cardsWon++
        }
        table.cardsOnTable.clear()
        return name
    }
}

/** A class used to manage cards on a table. */
class Table() {
    var cardsOnTable = mutableListOf<String>()
}

/** Run the main Indigo Card Game program. */
fun main() {
    val cardDeck = CardDeck(cardRanks, cardSuits)
    val playerOne = Player(true)
    val playerTwo = Player(false)
    val table = Table()
    val gameHandler = GameHandler()

    gameHandler.printGameTitle()
    gameHandler.decideFirstPlayer(playerOne, playerTwo)
    // println("P1 first: ${playerOne.isFirstPlayer}, P2 first: ${playerTwo.isFirstPlayer}")
    // println("P1 human: ${playerOne.isHumanPlayer}, P2 human: ${playerTwo.isHumanPlayer}")
    cardDeck.shuffleDeck()
    gameHandler.dealCards(cardDeck, table, playerOne, playerTwo)
    // gameHandler.displayActionMenu(cardDeck, table, playerOne, playerTwo)
    gameHandler.beginPlay(table)



    while (!gameHandler.keepPlaying) {
        if (playerOne.isFirstPlayer) {
            gameHandler.playerOneFirst(gameHandler, playerOne, playerTwo, table)
        } else {
            gameHandler.playerTwoFirst(gameHandler, playerOne, playerTwo, table)
        }

        if ((playerOne.cardsInHand.size == 0) && (playerTwo.cardsInHand.size == 0)) {
            if (cardDeck.cardsInDeck.size == 0) {
                gameHandler.announceCardsOnTable(table)
                if (table.cardsOnTable.size > 0) {
                    if (gameHandler.cardWinners.elementAt(gameHandler.cardWinners.size -1) == "Player") {
                        gameHandler.checkCardPoints(table)
                        playerOne.earnPoints(gameHandler, table)
                        playerOne.winCards(table)
                    } else {
                        gameHandler.checkCardPoints(table)
                        playerTwo.earnPoints(gameHandler, table)
                        playerTwo.winCards(table)
                    }
                }
                if (playerOne.cardsWon > playerTwo.cardsWon) {
                    playerOne.score += POINTS_FOR_MOST_CARDS
                } else if (playerOne.cardsWon == playerTwo.cardsWon) {
                    if (playerOne.isFirstPlayer) {
                        playerOne.score += POINTS_FOR_MOST_CARDS
                    } else {
                        playerTwo.score += POINTS_FOR_MOST_CARDS
                    }
                } else {
                    playerTwo.score += POINTS_FOR_MOST_CARDS
                }
                gameHandler.announceCurrentScore(playerOne, playerTwo)
                gameHandler.keepPlaying = gameHandler.exitGame()
            } else {
                gameHandler.dealCards(cardDeck, playerOne, playerTwo)
            }
        }

        if (gameHandler.keepPlaying) {
            break
        }
    }
}