import toast from 'react-hot-toast';
import UserForm from '../../components/Users/UserForm/UserForm';
import UserList from '../../components/Users/UserList/UserList';
import './Users.css';
import { useEffect, useState } from 'react';
import { fetchUsers } from '../../Service/UserService';

const Users = () => {
    const [users, setUsers] = useState([]);
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        async function getUserList() {
            try {
                setLoading(true);
                const response = await fetchUsers();
                setUsers(response.data);
            }
            catch (error) {
                console.error(error);
                toast.error("Unable to fetch users.");
            }
            finally {
                setLoading(false);
            }
        }

        getUserList();
    }, []);

    return (
        <div className="users-container text-light">
            <div className="left-column">
                <UserForm setUsers={setUsers} />
            </div>
            <div className="right-column">
                <UserList users={users} setUsers={setUsers} />
            </div>
        </div>
    )
}

export default Users;