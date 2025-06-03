import { useContext, useState } from 'react';
import './Explore.css';
import { AppContext } from '../../context/AppContext';
import DisplayCategory from '../../components/Explore/DisplayCategory/DisplayCategory';
import DisplayItem from '../../components/Explore/DisplayItem/DisplayItem';
import CustomerForm from '../../components/Explore/CustomerForm/CustomerForm';
import CartItems from '../../components/Explore/CartItems/CartItems';
import CartSummary from '../../components/Explore/CartSummary/CartSummary';

const Explore = () => {
    const { categories } = useContext(AppContext);
    const [selectedCategory, setSelectedCategory] = useState('');
    const [customerName, setCustomerName] = useState('')
    const [mobileNumber, setmobileNumber] = useState('')

    return (
        <div className="explore-container text-light">
            <div className="left-column">
                <div className="first-row" style={{ overflow: 'auto' }}>
                    <DisplayCategory
                        categories={categories}
                        selectedCategory={selectedCategory}
                        setSelectedCategory={setSelectedCategory}
                    />
                </div>
                <hr className="horizontal-line"></hr>
                <div className="second-row" style={{ overflow: 'auto' }}>
                    <DisplayItem
                        selectedCategory={selectedCategory}
                    />
                </div>
            </div>
            <div className="right-column d-flex flex-column">
                <div className="customer-form-container" style={{ height: '15%' }}>
                    <CustomerForm
                        customerName={customerName}
                        mobileNumber={mobileNumber}
                        setCustomerName={setCustomerName}
                        setmobileNumber={setmobileNumber}
                    />
                </div>
                <hr className="my-3 text-light"></hr>
                <div className="cart-item-container" style={{ height: '55%', overflowY: 'auto' }}>
                    <CartItems />
                </div>
                <div className="cart-summary-container" style={{ height: '30%' }}>
                    <CartSummary
                        customerName={customerName}
                        mobileNumber={mobileNumber}
                        setCustomerName={setCustomerName}
                        setmobileNumber={setmobileNumber}
                    />
                </div>
            </div>
        </div>
    )
}

export default Explore;