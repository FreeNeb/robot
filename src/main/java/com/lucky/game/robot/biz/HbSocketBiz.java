package com.lucky.game.robot.biz;

import com.lucky.game.core.util.StrRedisUtil;
import com.lucky.game.robot.dto.huobi.KLineDetailDto;
import com.lucky.game.robot.market.HuobiApi;
import com.lucky.game.robot.vo.huobi.SymBolsDetailVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author conan
 *         2018/5/11 14:20
 **/
@Component
@Slf4j
public class HbSocketBiz {

    @Autowired
    private HuobiApi huobiApi;

    @Autowired
    private RedisTemplate<String,String> redis;

    public List<String> getAllSymbol() {
        List<String> allSymbol = new ArrayList<>();
        List<SymBolsDetailVo> list = huobiApi.getSymbolsInfo();
        for (SymBolsDetailVo vo : list) {
            allSymbol.add(vo.getSymbols());
        }
        return allSymbol;
    }

    public void reciveKLine(KLineDetailDto dto) {
        String symbolId = StrRedisUtil.get(redis,dto.getId()+dto.getSymbol());
        if(StringUtils.isEmpty(symbolId)){
            StrRedisUtil.setEx(redis,dto.getId()+dto.getSymbol(),120,dto.getSymbol());
            log.info("dto={}", dto);
        }
    }
}
