<template>
    <div class="pop-btom">
			<div class="pop-top">
				<p class="top-text">
					{{appName}}
				</p>
			</div>
			<div class="pop-content">
				<!-- ../../assets/QR-code/TPM.png -->
				<img :src="imgUrl" />
				<div class="active">
					<img src="../assets/images/popimg/android@2x.png" />
					<span>用Android手機掃描二維碼下載APP</span>
				</div>
				<img class="showimg" src="../assets/images/popimg/arrow-curved@2x.png">
			</div>
		</div>
</template>
<script>
export default {
	name:'PopView',
	props:{
		imgUrl:String,
		appName:String,
	},
	created(){      
	}	
}
</script>

<style scoped lang="scss">
    .pop-btom{
        width:400px;
		height:400px;
		background:linear-gradient(360deg,rgba(0,66,121,1) 0%,rgba(16,103,175,1) 100%);
		box-shadow:0px 2px 12px 0px rgba(0,0,0,0.5);
		border-radius:4px;
		margin: 0; 
		padding: 0px;
	}
	.pop-top{
		width:400px;
		height:48px;
		background:rgba(255,255,255,1);
	}
	.top-text{
		width:200px;
		height:48px;
		font-size:18px;
		font-family:MicrosoftYaHei;
		color:rgba(74,74,74,1);
		line-height:48px;
		margin-left:16px;		
		overflow: hidden;
	}
	.pop-content{
		width: 400px;
		height: 352px;
	}
	.pop-content img{
		position: relative;
		width: 172px;
		height: 172px;
 		left: 114px;
		top: 75px;
	}
	.active{
		position: relative;
		left: 57px;
		top: 90px;
		height: 40px;		
	}
	.active img{
		position: static;
		width: 40px;
		height: 40px;
		float: left;
	}
	.active span{
		position: static;
		height: 40px;
		line-height: 40px;
		font-size:14px;
		font-family:MicrosoftYaHei;
		color:rgba(255,255,255,1);
		display: block;
		width: 300px;
		float: left;
	}
	.pop-content .showimg{
		position: relative;
		width: 32px;
		height: 63px;
		left: 310px;
		top: 5px;	
	}
</style>
