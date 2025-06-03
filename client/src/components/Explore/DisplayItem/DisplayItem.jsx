import { useContext, useState } from 'react';
import './DisplayItem.css';
import { AppContext } from '../../../context/AppContext.jsx'
import Item from '../../SubComponents/Item/Item.jsx';
import SearchBox from '../../SubComponents/SearchBox/SearchBox.jsx';

const DisplayItem = ({ selectedCategory }) => {
    const { itemsData } = useContext(AppContext);
    const [searchtext, setSearchText] = useState('');

    const filteredItems = itemsData && itemsData.filter(item => {
        if (!selectedCategory) {
            return true;
        }

        return item.categoryId === selectedCategory;
    }).filter(item => {
        return item.name.toLowerCase().includes(searchtext.toLowerCase());
    })

    return (
        <div className="p-3">
            <div className="d-flex justify-content-between align-items-center mb-4">
                <div></div>
                <div>
                    <SearchBox onSearch={setSearchText} />
                </div>
            </div>
            <div className="row g-3">
                {
                    filteredItems && filteredItems.map((item, index) => (
                        <div key={index} className="col-md-4 col-sm-6">
                            <Item
                                itemName={item.name}
                                itemPrice={item.price}
                                itemImage={item.imgUrl}
                                itemId={item.itemId}
                            />
                        </div>
                    ))
                }
            </div>
        </div>
    )
}

export default DisplayItem;