### **1. Install Prerequisites**

#### **Install VirtualBox on Windows**

- Download and install VirtualBox from the [official website](https://www.virtualbox.org/).
- VirtualBox is the default provider for Vagrant.

#### **Install Vagrant in WSL**

1. Open your Debian WSL terminal.
    
2. Download and install the Vagrant package for Linux:
    
    bash
    
    Copiar código
    
    `sudo apt update sudo apt install -y curl gnupg software-properties-common curl -fsSL https://apt.releases.hashicorp.com/gpg | sudo apt-key add - sudo apt-add-repository "deb [arch=amd64] https://apt.releases.hashicorp.com $(lsb_release -cs) main" sudo apt update sudo apt install -y vagrant`
    
3. Verify that Vagrant is installed:
    
    bash
    
    Copiar código
    
    `vagrant --version`
    

---

### **2. Configure WSL for VirtualBox**

Since VirtualBox runs on the Windows host, you need to expose it to WSL:

1. **Add the VAGRANT_WSL_ENABLE_WINDOWS_ACCESS variable** Add this environment variable in your WSL session to allow Vagrant to access Windows components:
    
    bash
    
    Copiar código
    
    `export VAGRANT_WSL_ENABLE_WINDOWS_ACCESS="1"`
    
2. **Set the Provider** By default, Vagrant will use VirtualBox installed on Windows. You don’t need to configure the provider unless you want to use a different one.
    

---

### **3. Start Your Vagrantfile**

1. **Navigate to Your Project Directory** In WSL, go to the directory where your `Vagrantfile` is located:
    
    bash
    
    Copiar código
    
    `cd /mnt/d/path/to/your/project`
    
2. **Initialize the Vagrant Environment** If you don’t already have a `Vagrantfile`, you can create one:
    
    bash
    
    Copiar código
    
    `vagrant init`
    
    Otherwise, skip this step if the `Vagrantfile` already exists.
    
3. **Start the Vagrant Machine** Start your Vagrant machine:
    
    bash
    
    Copiar código
    
    `vagrant up`
    
4. **Access the Vagrant Machine** SSH into the Vagrant machine:
    
    bash
    
    Copiar código
    
    `vagrant ssh`
    

---

### **4. Debugging Tips**

- **Check if Vagrant Can Access VirtualBox:**  
    If you encounter issues, ensure VirtualBox is accessible from WSL:
    
    bash
    
    Copiar código
    
    `VAGRANT_LOG=debug vagrant up`
    
- **Shared Folders:**  
    If you're using shared folders between WSL and Windows, ensure they're correctly mounted in your Vagrantfile:
    
    ruby
    
    Copiar código
    
    `config.vm.synced_folder "/mnt/d/project-folder", "/vagrant"`
    

---

### **5. Stop the Vagrant Machine**

To stop the machine:

bash

Copiar código

`vagrant halt`

---

### **6. Clean Up (Optional)**

To destroy the Vagrant machine:

bash

Copiar código

`vagrant destroy`

---

Would you like help configuring your `Vagrantfile` or troubleshooting specific errors?