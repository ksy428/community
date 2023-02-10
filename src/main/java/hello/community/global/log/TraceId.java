package hello.community.global.log;

import hello.community.exception.member.MemberException;
import hello.community.exception.member.MemberExceptionType;
import hello.community.global.util.SecurityUtil;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.UUID;

public class TraceId {
    private String id;
    private int level;//깊이


    public TraceId() {
        this.id = createId();
        this.level = 0;
    }

    public TraceId(String id, int level) {
        this.id = id;
        this.level = level;
    }

    private String createId() {

        try {
            SecurityUtil.getLoginMemberId();
        }catch (NullPointerException | ClassCastException | MemberException e ){ //로그인 안하고 접근 & signUp등일 경우 anonymousUser가 반환되므로 캐스팅이 불가능
            return String.format("[Anonymous: %S]",UUID.randomUUID().toString().substring(0,8));
        }
        return SecurityUtil.getLoginMemberId();


       /* Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(principal.equals("anonymousUser")) {
            return String.format("[Anonymous: %S]", UUID.randomUUID().toString().substring(0,8));
        }

        UserDetails loginMember = (UserDetails) principal;

        return loginMember.getUsername();*/

    }

    public TraceId createNextId(){
        return new TraceId(id, level + 1);
    }

    public TraceId createPreviousId(){
        return new TraceId(id, level - 1);
    }

    public boolean isFirstLevel(){
        return level == 0;
    }

    public String getId() {
        return id;
    }

    public int getLevel() {
        return level;
    }
}
