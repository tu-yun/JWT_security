package com.xi.fmcs.domain.moveReserve.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class MoveReserveExcelDto {

    private List<Map<String, Object>> datas;

    public MoveReserveExcelDto(List<MoveReserveListAllDto> moveReserve) {
        datas = new ArrayList<>();
        moveReserve.forEach(item -> {
            Map<String, Object> data = new HashMap<>();
            data.put("DONG",item.getDong());
            data.put("HO",item.getHo());
            data.put("MOVE_DATE",item.getMoveDate());
            data.put("MOVE_TIME",item.getMoveTime());
            data.put("REG_DATE",item.getRegDate());
            datas.add(data);
        });
    }

}
