package nil.ed.chatroom.controller;

import nil.ed.chatroom.common.PageResult;
import nil.ed.chatroom.common.Response;
import nil.ed.chatroom.entity.RoomEntity;
import nil.ed.chatroom.service.IRoomService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.Instant;
import java.util.List;

/**
 * @author delin10
 * @since 2019/10/14
 **/
@RestController
@RequestMapping("/chatroom")
public class RoomController {
    @Resource(name = "roomService")
    private IRoomService roomService;

    @PostMapping("/room/add")
    public Response<Void> addRoom(@RequestBody(required = false) RoomEntity room){
        room.setCreator(1L);
        return roomService.addRoom(room);
    }

    @PostMapping("/room/delete/{roomId}")
    public Response<Void> deleteRoom(@PathVariable("roomId") Long roomId){
        return roomService.deleteRoom(roomId, 1L);
    }

    @GetMapping("/room/list")
    public Response<PageResult<RoomEntity>> listRooms(){
        return roomService.listRooms();
    }

    @GetMapping("/room/{roomId}/user/list/rank")
    public Response<List<Object>> listRooms(@PathVariable("roomId") Long roomId){
        return roomService.listRankUsersTop50(roomId);
    }

}
