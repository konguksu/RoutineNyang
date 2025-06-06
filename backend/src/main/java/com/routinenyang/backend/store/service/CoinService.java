package com.routinenyang.backend.store.service;

import com.routinenyang.backend.global.exception.CustomException;
import com.routinenyang.backend.global.exception.ErrorCode;
import com.routinenyang.backend.store.dto.CoinResponse;
import com.routinenyang.backend.store.entity.Coin;
import com.routinenyang.backend.store.repository.CoinRepository;
import com.routinenyang.backend.user.entity.User;
import com.routinenyang.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.routinenyang.backend.global.exception.ErrorCode.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class CoinService {

    private final CoinRepository coinRepository;
    private final UserRepository userRepository;

    public CoinResponse getAmountByUserId(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new CustomException(USER_NOT_FOUND)
        );
        Coin coin = coinRepository.findByUser(user).orElseGet(
                () -> coinRepository.save(Coin.builder().user(user).build())
        );

        return CoinResponse.from(coin);
    }

    public void addCoinByUserId(Long userId, int amount) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new CustomException(USER_NOT_FOUND)
        );
        Coin coin = coinRepository.findByUser(user).orElseGet(
                () -> coinRepository.save(Coin.builder().user(user).build())
        );

        coin.addAmount(amount);
    }

    public void removeCoinByUserId(Long userId, int amount) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new CustomException(USER_NOT_FOUND)
        );
        Coin userCoin = coinRepository.findByUser(user).orElseGet(
                () -> coinRepository.save(Coin.builder().user(user).build())
        );

        // 코인이 부족하면 예외 발생
        if (userCoin.getAmount() < amount) {
            throw new CustomException(ErrorCode.COIN_NOT_ENOUGH);
        }

        userCoin.removeAmount(amount);
    }
}
