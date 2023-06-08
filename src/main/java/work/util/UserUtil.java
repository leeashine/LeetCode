package work.util;

import work.domain.User;

public class UserUtil {
    private UserUtil() {
    }

    private static ThreadLocal<User> userThreadLocal = new ThreadLocal<>();

    public static void putUser(User param) {
        userThreadLocal.set(param);
    }

    public static User getCurrentUser() {
        return userThreadLocal.get();
    }

    public static void removeCurrentUser() {
        userThreadLocal.remove();
    }

    public static Long getUserId() {
        User currentUser = getCurrentUser();
        return currentUser != null ? currentUser.getId() : null;
    }

}
