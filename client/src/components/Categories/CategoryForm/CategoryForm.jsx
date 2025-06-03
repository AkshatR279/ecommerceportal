import { useContext, useEffect, useState } from "react";
import { assets } from "../../../assets/assets";
import toast from "react-hot-toast";
import { AppContext } from '../../../context/AppContext.jsx'
import { addCategory } from "../../../Service/CategoryService.js";

const CategoryForm = () => {
    const { setCategories, categories } = useContext(AppContext);
    const [loading, setLoading] = useState(false);
    const [image, setImage] = useState(false);
    const [data, setData] = useState({
        name: '',
        description: '',
        bgColor: '#2c2c2c'
    });

    useEffect(() => {
        console.log(data);
    }, [data]);

    const onChangeHandler = (e) => {
        const value = e.target.value;
        const name = e.target.name;
        setData((data) => ({ ...data, [name]: value }));
    }

    const onSubmitHandler = async (e) => {
        e.preventDefault();

        if (!image) {
            toast.error("Please select an image for the category.");
            return;
        }

        setLoading(true);
        const formData = new FormData();
        formData.append('category', JSON.stringify(data));
        formData.append('file', image);

        try {
            const response = await addCategory(formData);
            if (response.status === 201) {
                setCategories([...categories, response.data]);
                toast.success('Category added successfully.');

                setData({
                    name: '',
                    description: '',
                    bgColor: '#2c2c2c'
                });
                setImage(false);
            }
            else {
                toast.error("Failed to add category.");
            }
        }
        catch (error) {
            toast.error("Failed to add category.");
            console.error(error);
        }
        finally {
            setLoading(false);
        }
    }

    return (
        <div className="mx-2 mt-2">
            <div className="row">
                <div className="card col-md-12 form-container">
                    <div className="card-body">
                        <form onSubmit={onSubmitHandler}>
                            <div className="mb-3">
                                <label htmlFor="image" className="form-label">
                                    <img src={image ? URL.createObjectURL(image) : assets.upload} alt="Upload Image" width={150}></img>
                                </label>
                                <input type="file" name="image" id="image" className="form-control" hidden
                                    onChange={(e) => setImage(e.target.files[0])} />
                            </div>
                            <div className="mb-3">
                                <label htmlFor="name" className="form-label">Name</label>
                                <input type="text" name="name" id="name" className="form-control" placeholder="Category Name"
                                    onChange={onChangeHandler} value={data.name} required />
                            </div>
                            <div className="mb-3">
                                <label htmlFor="description" className="form-label">Description</label>
                                <textarea name="description" id="description" className="form-control" placeholder="Category description...." rows="5"
                                    onChange={onChangeHandler} value={data.description} />
                            </div>
                            <div className="mb-3">
                                <label htmlFor="bgcolor" className="form-label">Background Color</label>
                                <br />
                                <input type="color" name="bgColor" id="bgColor" placeholder="#ffffff"
                                    onChange={onChangeHandler} value={data.bgColor} />
                            </div>
                            <button type="submit" className="btn btn-warning w-100"
                                disabled={loading}>{loading ? 'Loading..' : 'Submit'}</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    )
}

export default CategoryForm;