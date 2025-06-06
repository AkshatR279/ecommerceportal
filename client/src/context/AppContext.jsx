import { createContext, useEffect, useState } from "react";
import { fetchCategory } from '../Service/CategoryService.js'
import { fetchItems } from "../Service/ItemService.js";

export const AppContext = createContext(null);

export const AppContextProvider = (props) => {

    const [categories, setCategories] = useState();
    const [itemsData, setItemsData] = useState([]);
    const [auth, setAuth] = useState({ token: null, role: null });
    const [cartItems, setCartItems] = useState([]);

    const addToCart = (item) => {
        const exitsingItem = cartItems.find(cartItem => cartItem.name === item.name);
        if (exitsingItem) {
            setCartItems(cartItems.map(cartItem => cartItem.name == item.name ? { ...cartItem, quantity: cartItem.quantity + 1 } : cartItem));
        }
        else {
            setCartItems([...cartItems, { ...item, quantity: 1 }]);
        }
    }

    const removeFromCart = (itemId) => {
        setCartItems(cartItems && cartItems.filter(item => item.itemId !== itemId));
    }

    const updateCartQuantity = (itemId, newQuantity) => {
        setCartItems(cartItems && cartItems.map(item => item.itemId === itemId ? { ...item, quantity: newQuantity } : item));
    }

    useEffect(() => {
        async function loadData() {
            if (localStorage.getItem('token') && localStorage.getItem('role')) {
                setAuthData(localStorage.token, localStorage.role);
            }
            const response = await fetchCategory();
            const itemResponse = await fetchItems();
            setCategories(response.data);
            setItemsData(itemResponse.data);
        }

        loadData();
    }, []);

    const setAuthData = (token, role) => {
        setAuth({token : token, role: role});
    }

    const clearCart = () => {
        setCartItems([]);
    }

    const contextValue = {
        categories,
        setCategories,
        auth,
        setAuthData,
        itemsData,
        setItemsData,
        addToCart,
        cartItems,
        removeFromCart,
        updateCartQuantity,
        clearCart
    }

    return <AppContext.Provider value={contextValue}>
        {props.children}
    </AppContext.Provider>
}