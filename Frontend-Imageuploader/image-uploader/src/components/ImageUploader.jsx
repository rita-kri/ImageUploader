import React, { useEffect, useState } from "react";
import axios from 'axios';

function ImageUploader() {
//to create a statte for select the file
    const [selectedFile, setSelectedFile] = useState(null);
    const [files, setFiles] = useState([]); //files are coming from server
    //for loading the files and showing the msg
    const [messageStatus, setMessageStatus] = useState(false);
    const [uploading, setUploading] = useState(false);
    const [uploadedSrc, setUploadedSrc] = useState("");

    useEffect(()=>{
        getFiles();
    }, []);

    const getFiles = ()=>{
        const url =`http://localhost:8081/api/v1/s3/all`;
        axios.get(url)
        .then((response)=>{
            console.log(response.data);
            setFiles(response.data);
        })
        .catch((error)=>{
            console.log(error);
        });
    }

    const handleFileChange =(event) => {
        const file = event.target.files[0];
        console .log(file);
        if(file.type==='image/png' || file.type==='image/jpeg'){
            setSelectedFile(file);
        }else{
            alert("Select Image file only !!");
            setSelectedFile(null);
        }
        
    };
    const formSubmit =(event) => {
        event.preventDefault();
        if(selectedFile){
            //image upload then call one function
            uploadImageToServer();

        }else{
            alert("Select Image First");
        }
    };

    //function to upload image to server
    const uploadImageToServer =() =>{
        const url =`http://localhost:8081/api/v1/s3/upload`;
        const data = new FormData();
        data.append("file", selectedFile);
        setUploading(true);
        axios
        .post(url, data)
        .then((response) =>{
                console.log(response.data);
                setUploadedSrc(response.data);
                setMessageStatus(true);
                getFiles();
        })
        .catch((error)=>{
            console.log(error);
        })     
    .finally(
        ()=>{
            console.log("Request Finished !!");
            setUploading(false);
        }
    );
    }

    return (
        <div className="main flex flex-col items-center justify-center">
            <div className=" rounded w-1/3 border shadow m-4 p-4 bg-gray-100">
            <h1 className="text-2xl">Image Uploader</h1>
            <div className="form_container mt-3">

                <form action="" onSubmit={formSubmit}>
                    <div className="field_container flex flex-col gap-y-2">
                        <label htmlFor="file">Select Image</label>
                        <input onChange={handleFileChange} type="file" id="file" />
                    </div>
                    <div className="field_container text-center mt-3">
                        <button 
                        type="submit"
                        className="px-3 py-1 bg-blue-700 hover:bg-blue-600 text-white rounded">
                            Upload
                        </button>
                        <button 
                        type="reset"
                        className="ml-2 px-3 py-1 bg-orange-700 hover:bg-orange-600 text-white rounded">
                            Clear
                        </button>
                    </div>
                </form>
            </div>

            {/* message alert */}
            { messageStatus && (
                <>
                {/* <!-- Success Alert --> */}
                <div className="mt-3 bg-green-100 border border-green-400 text-green-700 px-4 py-3 rounded relative" 
                role="alert">
                <strong className="font-bold">Success!</strong>
                <span className="block sm:inline">Image is uploaded.</span>
                <span className="absolute top-0 bottom-0 right-0 px-4 py-3">
                    <svg className="fill-current h-6 w-6 text-green-500" 
                    role="button" xmlns="http://www.w3.org/2000/svg" 
                    viewBox="0 0 20 20"><title>Close</title><path d="M14.348 5.652a.5.5 0 0 0-.707 0L10 9.293 6.36 5.652a.5.5 0 1 0-.707.707L9.293 10l-3.64 3.641a.5.5 0 0 0 .707.707L10 10.707l3.641 3.64a.5.5 0 0 0 .707-.707L10.707 10l3.641-3.641a.5.5 0 0 0 0-.707z"/></svg>
                </span>
                </div>
                </>
            )}

            {/* uploading text and loader */}

            {uploading && (
                <div className="p-3 text-center">
                    <div className="mt-3 flex items-center justify-center bg-gray-100">
                    <div className="loader"></div>
                    </div>
                    <h1>Uploading ...</h1>
                </div>
            )}

            {/* uploaded image view */}
            {
                messageStatus && (
                <div className="upload_view">
                <img className="h-[200px] mx-auto mt-5 rounded shadow" 
                src= {uploadedSrc} 
                alt="" />
            </div>
           )}
            </div>
            {/* show uploaded images sections */}
            <div className="mt-4 px-4 justify-center flex flex-wrap">
                {files.map((img)=>(
                    <img className="h-[200px] m-1 shadow rounded" src={img} key={img} />
                ))}
            </div>
            {/* show uploaded images sections */}
            {/* <div className="mt-4 px-4 justify-center flex flex-wrap">
            {files.map((img) => (
                <div className="w-full md:w-1/2 lg:w-1/3 p-2" key={img}>
                <img className="w-full h-auto shadow rounded" src={img} alt="Uploaded" />
                </div>
            ))}
            </div> */}
        </div>
    );
}

export default ImageUploader;