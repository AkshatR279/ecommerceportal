import { useContext, useState } from "react";
import { AppContext } from "../../../context/AppContext";
import { deleteItem } from "../../../Service/ItemService";
import '../ItemList/ItemList.css';

const ItemList = () =>{
    const { itemsData, setItemsData } = useContext(AppContext);
    const [searchTerm, setSearchTerm] = useState('');

    const filteredItems = itemsData && itemsData.filter(item =>
        item.name.toLowerCase().includes(searchTerm.toLowerCase())
    );

    const deleteByItemId = async (itemId) => {
        try {
            const response = await deleteItem(itemId);
            if (response.status === 204) {
                const updatedItems = itemsData.filter(item => item.itemId !== itemId);
                setItemsData(updatedItems);

                toast.success("Item deleted successfully.");
            }
            else {
                toast.error("Unable to delete item.");
            }
        }
        catch (error) {
            console.error(error);
            toast.error("Unable to delete item.");
        }
    };

    return (
         <div className="category-list-container" style={{ height: '100vh', overflowY: 'auto', overflowX: 'hidden' }}>
            <div className="row pe-2">
                <div className="input-group mb-3">
                    <input type="text" name="keyword" id="keyword" placeholder="Search.." className="form-control"
                        onChange={(e) => setSearchTerm(e.target.value)} value={searchTerm}></input>
                    <span className="input-group-text bg-warning">
                        <i className="bi bi-search"></i>
                    </span>
                </div>
            </div>
            <div className="row g-3 pe-2">
                {filteredItems && filteredItems.map((item, index) => {
                    return (
                        <div key={index} className="col-12">
                            <div className="card p-3 bg-dark">
                                <div className="d-flex align-items-center">
                                    <div style={{ marginRight: '15px' }}>
                                        <img className="item-image" src={item.imgUrl} alt={item.name} />
                                    </div>
                                    <div className="flex-grow-1">
                                        <h5 className="mb-1 text-white">{item.name}</h5>
                                        <p className="mb-0 text-white">
                                            Category: {item.categoryName}
                                        </p>
                                        <span className="mb-0 text-block badge rounded-pill text-bg-warning">
                                            &#8377;{item.price}
                                        </span>
                                    </div>
                                    <div>
                                        <button className="btn btn-danger btn-sm" onClick={() => deleteByItemId(item.itemId)}>
                                            <i className="bi bi-trash"></i>
                                        </button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    );
                })}
            </div>
        </div>
    )
}

export default ItemList;