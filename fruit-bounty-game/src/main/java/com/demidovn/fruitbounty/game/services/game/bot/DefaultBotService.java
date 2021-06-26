package com.demidovn.fruitbounty.game.services.game.bot;

import com.demidovn.fruitbounty.game.converters.bot.MoveActionConverter;
import com.demidovn.fruitbounty.game.model.Pair;
import com.demidovn.fruitbounty.gameapi.model.Game;
import com.demidovn.fruitbounty.gameapi.model.Player;
import com.demidovn.fruitbounty.gameapi.services.BotService;
import java.time.Instant;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DefaultBotService implements BotService {

  private static final int BOT_ID = -2000;
  private static final int BOT_WAITING_MOVE_TIME = 1300;
  private static final String UNKNOWN_PERSON_IMG = "https://graph.facebook.com/107543663779701/picture";
  private static final int MAX_BOT_WINS = 10;
  private static final int MIN_BOT_DEFEATS = 20;
  private static final int MAX_BOT_DEFEATS = 100;

  private static final String TRAINER_NAME = "Trainer";
  private static final int TRAINER_SCORE = 2000;
  private static final int TRAINER_WINS = 10_000;
  private static final int TRAINER_DEFEATS = 1_000;
  private static final int TRAINER_DRAWS = 100;

  private final Random rand = new Random();

  @Autowired
  private MoveActionConverter moveActionConverter;

  @Autowired
  private BotNameGenerator botNameGenerator;

  @Autowired
  private BotMoveFinder botMoveFinder;

  private Integer minBotScore;
  private Integer maxBotScore;

  @Override
  public void setMinBotRating(int minBotScore) {
    this.minBotScore = minBotScore;
  }

  @Override
  public void setMaxBotRating(int maxBotScore) {
    this.maxBotScore = maxBotScore;
  }

  @Override
  public Player createNewBot() {
    Player bot = new Player();

    bot.setId(BOT_ID);
    bot.setImg(UNKNOWN_PERSON_IMG);

    bot.setPublicName(botNameGenerator.getRandomName());
    bot.setScore(generateRandomScore());

    bot.setWins(rand.nextInt(MAX_BOT_WINS));
    bot.setDefeats(rand.nextInt(MAX_BOT_DEFEATS - MIN_BOT_DEFEATS) + MIN_BOT_DEFEATS);

    return bot;
  }

  @Override
  public Player createTrainer() {
    Player bot = createNewBot();

    bot.setPublicName(TRAINER_NAME);
    bot.setScore(TRAINER_SCORE);

    bot.setWins(TRAINER_WINS);
    bot.setDefeats(TRAINER_DEFEATS);
    bot.setDraws(TRAINER_DRAWS);

    return bot;
  }

  @Override
  public boolean isPlayerBot(Player player) {
    return player.getId() == BOT_ID;
  }

  @Override
  public void actionIfBot(Game game) {
    if (game.isFinished() ||
      !isPlayerBot(game.getCurrentPlayer()) ||
      !isWaitEnoughTime(game)) {
      return;
    }

    Pair<Integer, Integer> generatedMove = botMoveFinder.findMove(game);

    game.getGameActions().add(
      moveActionConverter.convert2MoveAction(game, generatedMove.getKey(), generatedMove.getValue()));
  }

  private Integer getMinBotScore() {
    if (minBotScore == null) {
      throw new IllegalStateException("minBotScore should be initialized first");
    }

    return minBotScore;
  }

  private Integer getMaxBotScore() {
    if (maxBotScore == null) {
      throw new IllegalStateException("maxBotScore should be initialized first");
    }

    return maxBotScore;
  }

  private boolean isWaitEnoughTime(Game game) {
    return Instant.now().toEpochMilli() - game.getCurrentMoveStarted() > BOT_WAITING_MOVE_TIME;
  }

  private int generateRandomScore() {
    return rand.nextInt(getMaxBotScore() - getMinBotScore()) + getMinBotScore();
  }

}
