import axios from 'axios';

class PasswordService {
    updateMe(currentPassword, newPassword) {
        return axios.put(`/me/password`, {
            currentPassword: currentPassword,
            newPassword: newPassword
        });
    }

    updateUser(userId, currentPassword, newPassword) {
        return axios.put(`/users/${userId}/password`, {
            currentPassword: currentPassword,
            newPassword: newPassword
        });
    }
}

export default new PasswordService();