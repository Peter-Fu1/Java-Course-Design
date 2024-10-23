package campuscardsystem;

class User {
    protected String username;
    protected String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public boolean login(String inputUsername, String inputPassword) {
        return username.equals(inputUsername) && password.equals(inputPassword);
    }

    public void changePassword(String newPassword) {
        if (newPassword.length() >= 8 && !newPassword.equals(password)) {
            password = newPassword;
            System.out.println("密码修改成功。");
        } else {
            System.out.println("密码长度必须至少为8个字符，并且不能与旧密码相同。");
        }
    }
}
